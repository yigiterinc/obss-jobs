<template>
<div class="talent-scout">
    <Navbar></Navbar>
    <div class="prelude">
        <h1>Talent Scout</h1>
        <p>Search for any keyword, searches are done on name, email, skill fields.</p>
    </div>
    <div class="container">
        <div class="form-group search">
            <label for="search">Search anything</label>
            <div class="form-inline">
                <input type="text" class="form-control" id="search" placeholder="Search" v-model="query" required>
                <button class="btn btn-info" @click="search()">Search</button>
            </div>
        </div>

        <div class="card results-table">
            <table class="table table-hover">
                <thead class="thead-light">
                <tr>
                    <th scope="col">#</th>
                    <th scope="col">Name</th>
                    <th scope="col">Email</th>
                    <th scope="col">Blacklisted</th>
                    <th scope="col">View skills</th>
                    <th scope="col">View applications</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="(searchResult, i) in searchResults">
                    <th scope="row">{{ i + 1 }}</th>
                    <td scope="row"><router-link :to="getRedirectUrl(searchResult.candidateId)">{{ searchResult.fullName }}</router-link></td>
                    <td>{{ searchResult.email }}</td>
                    <td>{{ getBlacklistStatusText(searchResult.blacklisted) }}</td>
                    <td>
                        <button type="button" class="btn btn-light" @click="showSkills(searchResult)">View Skills</button>
                    </td>
                    <td>
                        <button type="button" class="btn btn-light" @click="showApplications(searchResult)">View Applications</button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
</template>

<script>
    import Navbar from "../Nav.vue"
    import ServicesMixin from "../mixins/services-mixin";

    export default {
        name: "TalentScout",
        mixins: [ServicesMixin],
        components: {
            Navbar
        },
        data () {
            return {
                searchResults: [],
                usersFound: {},
                query: ""
            }
        },
        methods: {
            search: function () {
                this.performFreeTextSearch(this.query).then(response => {
                    this.searchResults = [];
                    this.usersFound = [];

                    response.forEach(entry => {
                        let user = this.usersFound[this.getKey(entry)];

                        if (!user) {
                            this.usersFound[entry.fullName] = {
                                candidateId: entry.candidateId,
                                email: entry.email,
                                skills: new Set(),
                                blacklisted: entry.blacklisted
                            };
                            this.usersFound[entry.fullName].skills.add(entry.skill);
                        } else {
                            this.usersFound[entry.fullName].skills.add(entry.skill);

                            if (entry.blacklisted)
                                this.usersFound[entry.fullName].blacklisted = true;
                        }
                    });

                    Object.keys(this.usersFound).forEach(key => {
                        let foundUser = this.usersFound[key];

                        this.searchResults.push({
                            candidateId: foundUser.candidateId,
                            email: foundUser.email,
                            fullName: key,
                            skills: foundUser.skills,
                            blacklisted: foundUser.blacklisted
                        })
                    });
                });
            },
            getRedirectUrl: function (candidateId) {
                return "profile/" + candidateId;
            },
            getKey: function(user) {
                return user.fullName;
            },
            getBlacklistStatusText: function (status) {
                return status === true ? "Yes" : "No";
            },
            showSkills: function (user) {
                this.getLinkedInSkills(user.candidateId).then(skills => {
                    this.$swal("Skills of " + user.fullName, skills.toString(), "info")
                });
            },
            showApplications: function (user) {
                this.getCandidateApplications(user.candidateId).then(applications => {
                    let jobTitles = [];

                    applications.forEach(application => {
                        jobTitles.push(application.jobPostTitle)
                    });

                    this.$swal("Applications of " + user.fullName, jobTitles.toString(), "info");
                })
            },
        }
    }
</script>

<style scoped lang="scss">
    @import "../../styles/abstracts/typography";

    .talent-scout {
        @include set_font_montserrat();
    }

    #search {
        width: 500px;
        margin-right: 1rem;
    }

    .prelude {
        text-align: center;
        margin-top: 2rem;

        h1 {
            font-size: 50px;
            margin-bottom: 1.5rem;
        }
    }

    .form-group {
        margin-left: 15vw;
    }

    .table-hover {
        margin-bottom: 0;
    }

    .table {
        .btn {
            color: black;
            background-color: darken(#e9ecef, 5%);
        }
    }

    .results-table {
        margin-top: 10vh;
    }

    .search {
        margin-top: 5vh;
    }
</style>
