package com.services.obssjobs.service;

import com.services.obssjobs.model.entity.HrExpert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.core.support.BaseLdapNameAware;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Service;

import javax.naming.Name;
import javax.naming.directory.*;
import javax.naming.ldap.LdapName;
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
public class HrExpertService implements BaseLdapNameAware {

    private final LdapTemplate ldapTemplate;
    private LdapName baseLdapPath;

    @Autowired
    public HrExpertService(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    public void setBaseLdapPath(LdapName baseLdapPath) {
        this.baseLdapPath = baseLdapPath;
    }

    public void create(HrExpert p) {
        Name dn = buildDn(p);
        ldapTemplate.bind(dn, null, buildAttributes(p));
    }

    public List<HrExpert> findAll() {
        EqualsFilter filter = new EqualsFilter("objectclass", "person");
        return ldapTemplate.search(LdapUtils.emptyLdapName(), filter.encode(), new PersonContextMapper());
    }

    public HrExpert findOne(String uid) {
        Name dn = LdapNameBuilder.newInstance(baseLdapPath)
                                .add("ou", "people")
                                .add("uid", uid)
                                .build();
        return ldapTemplate.lookup(dn, new PersonContextMapper());
    }

    public List<HrExpert> findByName(String name) {
        LdapQuery q = query()
                .where("objectclass").is("person")
                .and("cn").whitespaceWildcardsLike(name);
        return ldapTemplate.search(q, new PersonContextMapper());
    }

    public void update(HrExpert p) {
        ldapTemplate.rebind(buildDn(p), null, buildAttributes(p));
    }

    public void updateLastName(HrExpert p) {
        Attribute attr = new BasicAttribute("sn", p.getLastName());
        ModificationItem item = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr);
        ldapTemplate.modifyAttributes(buildDn(p), new ModificationItem[] {item});
    }

    public void delete(HrExpert p) {
        ldapTemplate.unbind(buildDn(p));
    }

    private Name buildDn(HrExpert p) {
        return LdapNameBuilder.newInstance(baseLdapPath)
                .add("ou", "people")
                .add("uid", p.getUid())
                .build();
    }

    private Attributes buildAttributes(HrExpert p) {
        Attributes attrs = new BasicAttributes();
        BasicAttribute ocAttr = new BasicAttribute("objectclass");
        ocAttr.add("top");
        ocAttr.add("person");
        attrs.put(ocAttr);
        attrs.put("ou", "people");
        attrs.put("uid", p.getUid());
        attrs.put("cn", p.getFullName());
        attrs.put("sn", p.getLastName());
        attrs.put("password", p.getPassword());
        return attrs;
    }

    public boolean authenticate(String name, String password) {
        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectclass", "person"))
                .and(new EqualsFilter("cn", name))
                .and(new EqualsFilter("password", password));

        List<HrExpert> searchResult = ldapTemplate.search(LdapUtils.emptyLdapName(),
                                                            filter.encode(),
                                                            new PersonContextMapper());

        return searchResult.size() > 0;
    }

    private static class PersonContextMapper extends AbstractContextMapper<HrExpert> {
        public HrExpert doMapFromContext(DirContextOperations context) {
            HrExpert person = new HrExpert();
            person.setFullName(context.getStringAttribute("cn"));
            person.setLastName(context.getStringAttribute("sn"));
            person.setUid(context.getStringAttribute("uid"));
            return person;
        }
    }
}
