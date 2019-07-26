<template>
    <div class="job-details" v-if="dataFetched">
        <Nav></Nav>
        <header class="header"></header>
        <br>
        <div class="post content">
            <h1 class="post__title" @input="changeContentEdited(0)" :contenteditable="isHrExpert()">
                {{jobDetails.title}}</h1>
            <img src="../../assets/images/obss-logo.png" alt="obss-logo" class="logo">
            <p class="location">{{ getLocationText() }}</p>
            <div class="short-info">
                <p>Location: {{this.jobDetails.location}}</p>
                <p>Functional Area: Engineering, R & D</p>
                <p>Industry: Software</p>
            </div>
            <div class="description">
                <p class="description_label">Job Description</p>
                <p class="post__description" @input="changeContentEdited(1)" :contenteditable="isHrExpert()">
                    {{jobDetails.description}}</p>
            </div>
            <div>
                <p class="invite"><strong>If you think you satisfy these requirements:</strong></p>
                <ul v-for="(requirementObject, i) in jobRequirements" class="post__requirements"
                    @input="changeRequirementEdited(i)" :contenteditable="isHrExpert()"
                    :id="requirementObject.id">
                    <li>{{requirementObject.requirement}}</li>
                </ul>
                <p class="invite-finish"><strong>We would like to see you in our team!</strong></p>
            </div>
            <b-card bg-variant="dark" border-variant="info"
                    header="Follow your dreams" text-variant="white" class="bottom-container">
                <b-card-text>
                    <b-button type="button" class="btn-lg btn-info apply__button"
                                                                        v-on:click="saveIfAuthenticated()">
                      Apply Now
                    </b-button>
                    <p class="login-prompt"><em>*You need to log in with your LinkedIn account before applying</em></p>
                </b-card-text>
            </b-card>
        </div>
        <div>
            <div class="dates">
                <p v-if="!isHrExpert()" class="activation_date" v-model="jobDetails.activationDate"
                   :contenteditable="isHrExpert()">
                    <em>Posted at: {{ getActivationDate() }}</em>
                </p>
                <div v-else>
                    <p>Activation date</p>
                    <input type="date" name="activationdate" id="activation-date" v-model="activationDate" @input="changeContentEdited(2)">
                </div>
                <p v-if="!isHrExpert()" class="expiration_date" v-model="jobDetails.expirationDate"
                   :contenteditable="isHrExpert()">
                    <em>Expires at: {{ getExpirationDate() }}</em>
                </p>
                <div v-else>
                    <p>Expiration Date</p>
                    <input  type="date" name="expirationdate" id="expiration-date" v-model="expirationDate" @input="changeContentEdited(3)">
                </div>
            </div>
            <div class="form-check is-active" v-if="isHrExpert()">
                <input type="checkbox" class="form-check-input is_active" id="isActive"
                       :checked="jobDetails.isActive" v-model="jobDetails.isActive" @input="changeContentEdited(4)">
                <label class="form-check-label" for="isActive">Job Post is active</label>
            </div>

            <b-button v-if="isHrExpert()" type="button"
                      class="btn-lg btn-danger save-changes"
                      v-on:click="submitChanges()">
                Save changes
            </b-button>
        </div>
    </div>
</template>

<script>
    import Nav from "../Nav.vue";
    import ServicesMixin from "../mixins/services-mixin";
    import CommonsMixin from "../mixins/commons-mixin";

    export default {
        name: "JobApply",
        mixins: [ServicesMixin, CommonsMixin],
        components: {
            Nav: Nav
        },
        data() {
            return {
                jobDetails: {},
                jobRequirements: [],
                location: "Istanbul (Teknopark)",
                dataFetched: false,
                userLoggedIn: true,
                anyContentIsEdited: false,
                candidateBlacklisted: false,
                activationDate: "",
                expirationDate: "",
                linkedInLogin: "https://www.linkedin.com/oauth/v2/authorization?response_type=code&client_id=86uobfdmclzprd" +
                                "&redirect_uri=http://localhost:8080" +
                                "&state=ASk220aSFAxx&scope=r_liteprofile%20r_emailaddress%20w_member_social",
                contentEditableClasses: [
                    { className: 'post__title', edited: false },
                    { className: 'post__description', edited: false },
                    { className: 'activation_date', edited: false },
                    { className: 'expiration_date', edited: false },
                    { className: 'is_active', edited: false}
                ]
            }
        },
        computed: {
            userAuthenticated() {
                return this.$store.getters.userAuthenticated;
            },
            hrExpertAuthenticated() {
                return this.$store.getters.hrExpertAuthenticated;
            }
        },
        methods: {
            getJobPostId: function () {
                const url = window.location.pathname;
                // return the first url param
                return url.split("/")[2];
            },
            isHrExpertUrl: function () {
                const url = window.location.pathname;
                return url.split("/")[3] === 'hr';
            },
            isHrExpert: function () {
                return this.isHrExpertUrl() && this.$store.getters.hrExpertAuthenticated;
            },
            saveApplication: function () {
                if (!this.candidateBlacklisted) {
                    const userId = this.$store.state.userId;

                    try {
                        this.saveApplicationWithIdAndJobPost(userId, this.getJobPostId())
                            .then((response) => {
                                if (response.status === 200) {
                                    // If application already exists
                                    this.showApplicationSavedAlert();
                                    this.redirectToJobPostsAfterWaiting();
                                } else {
                                    this.showApplicationAlreadyExistsAlert();
                                }
                            });
                    } catch (e) {
                        console.log(e.status);
                    }
                } else {
                    this.showCandidateBlacklistedAlert();
                }
            },
            submitChanges: function () {
                const contentEditableClasses = this.contentEditableClasses;

                if (this.anyContentIsEdited) {
                        let title = document.getElementsByClassName(contentEditableClasses[0].className)[0].innerText;
                        let description = document.getElementsByClassName(contentEditableClasses[1].className)[0].innerText;

                        let activationDate = this.activationDate === "" ?
                            this.getDateFormatted(this.jobDetails.activationDate) :
                            this.getDateFormatted(this.convertStringToParseableDate(this.activationDate));

                        let expirationDate = this.expirationDate === "" ?
                            this.getDateFormatted(this.jobDetails.expirationDate) :
                            this.getDateFormatted(this.convertStringToParseableDate(this.expirationDate));

                        let active = this.jobDetails.isActive;

                        this.updateJobPostDetails(this.getJobPostId(), title, active,
                                                  description, activationDate, expirationDate);
                        this.updateRequirements();
                        this.showSuccessAlert();
                    } else {
                    this.showNoContentIsEditedAlert();
                }
            },
            updateRequirements: function () {
                this.jobRequirements.forEach(requirementObject => {
                    if (requirementObject.edited === true) {
                        const requirementEdited = document.getElementById(requirementObject.id).innerText;
                        let urlifiedRequirement = requirementEdited.replace(/%20/g, " ");

                        this.updateJobRequirementWithId(requirementObject.id, urlifiedRequirement);
                    }
                });
            },
            saveIfAuthenticated: function () {
                if (this.userAuthenticated && !this.isHrExpert()) {
                    this.saveApplication()
                } else {
                    this.showNotLoggedInAlert();
                    setTimeout(() => {
                        window.location.href =  this.linkedInLogin;
                    }, 2000)
                }
            },
            changeContentEdited: function (index) {
                this.contentEditableClasses[index].edited = true;
                this.anyContentIsEdited = true;
            },
            changeRequirementEdited: function(index) {
                this.jobRequirements[index].edited = true;
                this.anyContentIsEdited = true;
            },
            showSuccessAlert: function () {
                this.$swal('Saved', 'Your changes are successfully saved', 'success');
            },
            showApplicationSavedAlert: function () {
                this.$swal('Your application is taken', 'Your application is taken, good luck', 'success');
            },
            showApplicationAlreadyExistsAlert: function () {
                this.$swal('Already applied', 'You have already applied for this job.', 'error');
            },
            showNoContentIsEditedAlert: function () {
                this.$swal('No edit', 'Content is not edited' , 'warning');
            },
            showFailureAlert: function () {
                this.$swal('Error', 'An error occurred' , 'error');
            },
            showCandidateBlacklistedAlert: function () {
                this.$swal('Banned', 'You are banned from applying for jobs!', 'error');
            },
            showNotLoggedInAlert: function () {
                this.$swal('You are not logged in', 'You need to log in before applying, redirecting to login');
            },
            redirectToJobPostsAfterWaiting: function () {
                setTimeout(() => this.$router.push('/job-posts'), 3000);
            },
            fetchCandidateBlacklistStatus: function (candidateId) {
                this.getCandidateBlacklistStatus(candidateId).then(blacklistStatus => {
                    this.candidateBlacklisted = blacklistStatus;
                })
            },
            fetchJobRequirements: function (jobId) {
                this.getJobRequirements(jobId).then(jobRequirements => {
                    jobRequirements.forEach(jobRequirementsDto => {
                        this.jobRequirements.push({
                            id: jobRequirementsDto.id,
                            requirement: jobRequirementsDto.requirement,
                            edited: false
                        });
                    })
                });
            },
            fetchJobDetails: function (jobId) {
                this.getJobDetails(jobId).then(jobDetails => {
                    this.jobDetails = jobDetails;
                });
            },
            getLocationText: function () {
                const jobLocation = this.jobDetails.location;

                if (jobLocation) {
                    if (jobLocation.toLowerCase() === "istanbul") {
                        return "Istanbul (Teknopark)";
                    } else if (jobLocation.toLowerCase() === "izmit" || jobLocation.toLowerCase() === "kocaeli") {
                        return "Kocaeli (KOÜ)";
                    } else if (jobLocation.toLowerCase() === "ankara") {
                        return "Ankara (Bilkent Cyberpark)";
                    }
                }

                return jobLocation;
            },
            convertStringToParseableDate: function (str) {
                return str.replace("/-","/")
            },
            getDateFormatted: function (date) {  // @Param: Date in YYYY-MM-DD format and returns in DD-MM-YYYY format
                if (typeof date !== "string")
                    date = date.toString();

                const day = parseInt(date.split("-")[2]) + 1;
                const month = date.split("-")[1];
                const year = date.split("-")[0];

                return day + "/" + month + "/" + year;
            },
            getActivationDate: function() {
                if (this.jobDetails.activationDate)
                    return this.getDDMMYYFormatFromYYYYMMDD(this.jobDetails.activationDate);
            },
            getExpirationDate: function () {
                if (this.jobDetails.expirationDate)
                    return this.getDDMMYYFormatFromYYYYMMDD(this.jobDetails.expirationDate);
            }
        },
        mounted () {
            const jobId = this.getJobPostId();
            this.fetchJobRequirements(jobId);
            this.fetchJobDetails(jobId);
            this.fetchCandidateBlacklistStatus(this.$store.state.userId);

            this.dataFetched = true;
        }
    }
</script>

<style scoped lang="scss">
    @import '../../styles/abstracts/colors';
    @import '../../styles/abstracts/typography';

    .header {
        background-image: url("../../assets/images/thebay.jpg");
        background-size: cover;
        background-position: center;
        height: 45vh;
        width: 100vw;
        position: relative;
        margin-bottom: 0.75rem;
        @include set_font_montserrat();
        object-fit: cover;
    }

    .job-details {
        p {
            color: $slightly-blue-black;
        }

        .location {
            color: gray;
            font-size: 1.5rem;
            letter-spacing: .001rem;
            margin-bottom: 2rem;
        }

        .short-info {
            p {
                margin-bottom: 0;
                letter-spacing: .001rem;
                font-size: 16px;
                font-weight: 400;
                text-align: left;
            }

            margin-bottom: 1rem;
        }

        .apply__button {
            margin-top: 2rem;
            padding-right: 90px !important;
            padding-left: 90px !important;

            &:hover, &:link, &:active, &:focus {
                background-color: $color-complement-1;
            }
        }
    }

    .logo {
        max-width: 170px;
        max-height: 200px;
        margin-bottom: 1rem;
    }

    .content {
        margin-left: 3rem;
        width: 50vw;
    }

    .post__title {
        font-size: 2rem;
        margin-bottom: 1rem;
        text-align: left;
        color: $slightly-blue-black;
    }

    .post__description {
        margin-bottom: 2rem;
    }

    .description_label {
        font-size: 1.5rem;
        font-weight: 500;
    }

    .dates {
        p {
            margin-bottom: 0;
        }

        margin-top: 2rem;
        margin-left: 3rem;
        margin-bottom: 1rem;
    }

    .bottom-container {
        height: 15rem;
        right: 0;
        left: 50vw;
        top: 45vh;
        width: 40vw;
        position: absolute;
        text-align: center;
    }

    .btn-info {
        background: $color-complement-1;
    }

    .link {
        @include remove_link_styles;
    }

    li {
        margin-left: 1rem;
        color: $slightly-blue-black;
    }

    .is-active {
        margin-top: 1rem;
        margin-left: 3rem;
        margin-bottom: 15px;
    }

    .bottom-container__shadow {
        height: 6rem;
        background: #000;
    }

    ul {
        margin-bottom: 0;
        padding-left: 0;
    }

    .post__requirements--invite {
        margin-bottom: 1rem;
    }

    .invite-finish {
        margin-top: 1rem;
    }

    .card-header {
        height: 4rem;
        font-size: 25px;
        letter-spacing: 0.2rem;
    }

    .card-body {
        background-color: #e5e5e5;
        padding-bottom: 0;
    }

    .login-prompt {
        margin-top: 30px;
        font-size: 15px;
    }

    .save-changes {
        margin-left: 18rem;
        margin-bottom: 2rem;
    }

    [type="date"] {
        background:#fff url(https://cdn1.iconfinder.com/data/icons/cc_mono_icon_set/blacks/16x16/calendar_2.png)  97% 50% no-repeat ;
    }

    [type="date"]::-webkit-inner-spin-button {
        display: none;
    }

    [type="date"]::-webkit-calendar-picker-indicator {
        opacity: 0;
    }

    #activation-date {
        margin-bottom: 1rem;
    }

    input[type="date"] {
        border: 1px solid #c4c4c4;
        border-radius: 5px;
        background-color: #fff;
        padding: 3px 5px;
        box-shadow: inset 0 3px 6px rgba(0,0,0,0.1);
        width: 190px;
    }
</style>
