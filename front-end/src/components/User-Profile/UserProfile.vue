<template>
    <div class="user-profile" v-if="dataFetched">
        <Navbar></Navbar>
        <div class="container-box">
            <div class="profile-info">
                <br><br>
                <img :src=imageSource alt="">

                <h2 class="welcome-message">Welcome, {{fullName}}!</h2>
                <div class="skills">
                    <br><br>
                    <h2>Skills</h2>
                    <hr>

                    <ol v-if="skills">
                        <li v-for="skill in skills" class="skill"> {{ skill }} </li>
                    </ol>
                    <p v-else> Loading the skills.. </p>

                    <button class="btn btn-danger add-skill-button" v-if="this.$store.getters.hrExpertAuthenticated"
                            @click="showBlacklistInput()">
                        Blacklist User
                    </button>
                    <div v-else>
                        <button v-if="skillsToAdd.length === 0"
                                class="btn btn-primary add-skill-button"
                                @click="addSkill()">
                            {{getAddSkillButtonText()}}
                        </button>
                        <div v-else class="add-skill">
                            <input type="text" class="form-control skill-input" placeholder="Enter skill"
                                   v-model="skillsToAdd[i]" v-for="(_, i) in skillsToAdd">
                            <div class="buttons">
                                <button class="btn btn-danger" @click="addSkill()">+</button>
                                <button class="btn btn-danger save-button"
                                        v-on:click="saveSkills()"
                                        :disabled="!canSubmit">Submit</button>
                            </div>
                        </div>
                    </div>
                    <div v-if="blacklisting">
                        <div class="input-group input-reason">
                            <div class="input-group-prepend">
                                <span class="input-group-text">Reason</span>
                            </div>
                            <textarea class="form-control reason-textbox" aria-label="Enter reason"
                                      v-model="blacklistReason"></textarea>
                        </div>
                        <button class="btn btn-danger save-blacklist" @click="addCandidateToBlacklist()">Save</button>
                    </div>
                </div>
            </div>

            <div class="applications">
                <h2 class="applications__title">Submitted Applications</h2>
                <div class="row applications__row">
                    <div class="col-lg applications__col" v-for="application in this.candidateApplications">
                        <router-link :to=getJobPostUrlByJobId(application.jobPostId) class="applications__job-title">
                            {{ application.jobPostTitle }}
                        </router-link>
                        <p class="applications__small-info">OBSS, {{ application.location }}</p>
                        <div style="height: 10px;"></div>
                        <p>Application Status: {{ application.applicationStatus }}</p>
                        <p>Applied at: {{ application.applicationDate.split("T")[0].split("-").reverse().join("-") }}</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import ServicesMixin from "../mixins/services-mixin";
    import CommonsMixin from "../mixins/commons-mixin";
    import Navbar from "../Nav.vue";

    export default {
        name: "UserProfile",
        mixins: [ServicesMixin, CommonsMixin],
        components: {
            Navbar
        },
        data() {
            return {
                fullName: "",
                imageSource: "",
                skills: null,
                candidateApplications: [],
                skillsToAdd: [],
                NUMBER_OF_ROWS: 1,
                NUMBER_OF_COLS: 3,
                blacklisting: false,
                blacklistReason: "",
                dataFetched: false,
                skillAdderActivated: false
            }
        },
        methods: {
            getCandidateId: function() {
                const url = window.location.pathname;
                // return the first url param
                return url.split("/")[2];
            },
            getJobPostUrlByJobId: function (jobId) {
                let redirectUrl = '/job-details/' + jobId;

                if (this.$store.getters.hrExpertAuthenticated) {
                    return redirectUrl + '/hr';
                }

                return redirectUrl + '/us';
            },
            revertSkillAdderVisibility() {
                this.skillAdderActivated = !this.skillAdderActivated;
            },
            getAddSkillButtonText() {
                if (this.skillAdderActivated) {
                    return "Close";
                } else {
                    return "Add a skill";
                }
            },
            addSkill: function () {
                this.skillsToAdd.push("");
            },
            saveSkills: function () {
                if (this.doesCandidateHaveDuplicateSkillToAdd()) {
                    this.showCandidateAlreadyHasThisSkillAlert();
                } else {
                    let skills = this.skills.concat(this.skillsToAdd);

                    this.skills = null;
                    this.skillsToAdd = [];

                    this.submitLinkedInSkills(this.getCandidateId(), skills).then(() => {
                        this.getSkills();
                    })
                }
            },
            getSkills: function () {
                return new Promise((resolve, reject) => {
                    this.getLinkedInSkills(this.getCandidateId()).then(skills => {
                        this.skills = skills;
                        resolve();
                    }).catch(error => reject(error));
                });
            },
            doesCandidateHaveDuplicateSkillToAdd: function () {
                let duplicateFound = false;

                this.skillsToAdd.forEach(skillToAdd => {
                    if (this.skills.some(skill => skill.toLowerCase() === skillToAdd.toLowerCase())) {
                        duplicateFound = true;
                        return true;
                    }
                });

                return duplicateFound;
            },
            showCandidateAlreadyHasThisSkillAlert: function () {
                this.$swal('Skill already exist', 'You already have this skill' , 'warning');
            },
            showBlacklistInput: function () {
                this.blacklisting = !this.blacklisting;
            },
            addCandidateToBlacklist: function () {
                const id = window.location.pathname.split("/")[2];
                this.blacklistUser(id, this.blacklistReason);
                this.showSavedAlert();
            },
            showSavedAlert: function () {
                this.$swal('Success', 'User is blacklisted', 'success');
            }
        },
        computed: {
            hrExpertAuthenticated() {
                return this.$store.getters.hrExpertAuthenticated;
            },
            canSubmit () {
                return this.skillsToAdd.every(skill => !!skill);
            }
        },
        mounted() {
            this.getCandidateProfileDto(this.getCandidateId())
                .then(candidateProfileDto => {
                    this.fullName = candidateProfileDto.fullName;
                    this.imageSource = candidateProfileDto.imageUrl;
                });

            this.getCandidateApplications(this.getCandidateId())
                .then(candidateApplications => {
                    this.NUMBER_OF_ROWS = Math.ceil(candidateApplications.length / 3);
                    this.candidateApplications = candidateApplications;
                });

            this.getSkills();
            this.dataFetched = true;
        }
    }
</script>

<style scoped lang="scss">
@import "../../styles/abstracts/colors.scss";
@import "../../styles/abstracts/typography.scss";

.user-profile {
    color: $slightly-blue-black;
    @include set_font_opensans();
}

.profile-info {
    margin-left: 2vw;

    img {
        max-width: 100px;
        max-height: 100px;
        border-radius: 200px;
        margin-right: 10px;
    }
}

h2 {
    font-size: 25px;
}

.welcome-message {
    display: inline;
    font-size: 20px;
    margin-left: 1rem;
}

.applications {
    border-top: solid 2px darken(rgba(129, 180, 255, 0.27), 5%);
    background: rgba(129, 180, 255, 0.27);
    width: 100%;
    padding-right: 20px;
    padding-left: 20px;
    margin-top: 2rem;
    margin-bottom: 0;

    .applications__small-info {
        margin-top: 0;
    }
}

.applications__row {
    margin-left: 1rem;
    height: 320px;
}

.applications__col {
    padding: 15px;
    background: white;
    height: 240px;
    margin-right: 2rem;
    margin-bottom: 15px;
    border: solid 2px rgba(129, 180, 255, 0.9);
    font-size: 15px;

    p {
        margin-top: 15px;
        margin-bottom: 15px;
    }
}

.applications__title {
    margin-left: 1rem;
    margin-top: 1rem;
    margin-bottom: 2rem;
}

.applications__job-title {
    font-size: 25px;
    margin-bottom: 0;
}

.blacklist-button {
    margin-left: 20px;
}

.skill {
    margin-bottom: 10px;
}

.skills {
    ol {
        margin-bottom: 30px;
        padding-left: 10px;
    }
}

li {
    margin-left: 1rem;
    font-size: 18px;
}

.skill-input {
    margin-top: 1.5rem;
    width: 20vw;
}

.buttons {
    margin-top: 1rem;
    button {
        margin-right: 0.5rem;
    }
}

.input-reason {
    margin-top: 1rem;
    width: 50rem;
}

.save-blacklist {
    margin-top: 1rem;
}
</style>
