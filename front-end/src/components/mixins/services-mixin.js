import Vue from 'vue'

import axios from 'axios'
import VueAxios from 'vue-axios'

Vue.use(VueAxios, axios);

axios.defaults.headers.post['Access-Control-Allow-Origin'] = '*';

export default {
    data() {
        return {
            SERVICES_ENDPOINT: 'http://localhost:8888/api/services/controller',
            LINKED_IN_CONTROLLER_ENDPOINT: '/linked-in',
            JOB_POST_ENDPOINT: '/job-post',
            ALL_POSTS_ENDPOINT: '/posts',
            JOB_DETAILS_ENDPOINT: '/job-details',
            APPLICATION_ENDPOINT: '/application',
            HR_EXPERT_ENDPOINT: '/hr-expert',
            JOB_REQUIREMENTS_ENDPOINT: '/job-requirements',
            GET_HR_EXPERT_DTO_ENDPOINT: '/hr-admin',
            GET_CANDIDATE_PROFILE_DTO_ENDPOINT: '/candidate-profile',
            GET_CANDIDATE_APPLICATIONS_DTO_ENDPOINT: '/candidate-applications',
            LINKED_IN_SKILLS_ENDPOINT: '/linked-in-skills',
            SEARCH_ENDPOINT: '/search',
            GET_UPDATE_JOB_REQUIREMENT_ENDPOINT: () => this.SERVICES_ENDPOINT +
                this.JOB_REQUIREMENTS_ENDPOINT + '/update',
            GET_UPDATE_JOB_POST_ENDPOINT: () => this.SERVICES_ENDPOINT + this.JOB_POST_ENDPOINT + '/update',
            GET_JOB_POSTS_DTO_ENDPOINT: () => this.SERVICES_ENDPOINT + this.JOB_POST_ENDPOINT + this.ALL_POSTS_ENDPOINT,
            GET_JOB_DETAILS_DTO_ENDPOINT: jobId => {
                return this.SERVICES_ENDPOINT + this.JOB_POST_ENDPOINT + this.JOB_DETAILS_ENDPOINT +
                       '?jobId=' + jobId;
            },
        }
    },
    methods: {
        getJobPosts: function () {
            const endpoint = this.GET_JOB_POSTS_DTO_ENDPOINT();

            return this.get(endpoint);
        },
        getJobDetails: function (jobId) {
            const endpoint = this.GET_JOB_DETAILS_DTO_ENDPOINT(jobId);

            return this.get(endpoint);
        },
        getJobRequirements: function (jobId) {
            const endpoint = this.SERVICES_ENDPOINT + this.JOB_REQUIREMENTS_ENDPOINT
                            + '?jobId=' + jobId;

            return this.get(endpoint);
        },
        getHrExpertDtoList: function () {
            const endpoint = this.SERVICES_ENDPOINT + this.JOB_POST_ENDPOINT + this.GET_HR_EXPERT_DTO_ENDPOINT;

            return this.get(endpoint);
        },
        updateJobRequirementWithId: function (requirementId, newRequirement) {
            const endpoint = this.GET_UPDATE_JOB_REQUIREMENT_ENDPOINT() +
                             '?requirementId=' + requirementId + '&newRequirement=' + newRequirement;

            return this.put(endpoint);
        },
        saveJobPost: function (title, location, isActive,
                               createdBy, imageSrc, jobDescription,
                               activationDate, expirationDate) {

            const endpoint = this.SERVICES_ENDPOINT + this.JOB_POST_ENDPOINT
                + '/save?' + 'title=' + title + '&location=' + location + '&isActive=' + isActive
                + '&createdBy=' + createdBy + '&imageSrc=' + imageSrc + '&jobDescription=' + jobDescription
                + '&activationDate=' + activationDate + '&expirationDate=' + expirationDate;

            return new Promise ((resolve, reject) => {
                Vue.axios.post(endpoint).then((response) => {
                    resolve(response)
                }).catch(error => {
                    console.error(error);
                    reject(error);
                })
            })
        },
        updateJobPostDetails: function (jobPostId, title, isActive, jobDescription,
                                        activationDate, expirationDate) {

            const endpoint = this.SERVICES_ENDPOINT + this.JOB_POST_ENDPOINT
                            + '/update-details?' + 'jobPostId=' + jobPostId + '&title=' + title
                            + '&isActive=' + isActive + '&jobDescription=' + jobDescription
                            + '&activationDate=' + activationDate + '&expirationDate=' + expirationDate;

            return this.put(endpoint);
        },
        saveRequirementForJobPost: function (jobId, requirement) {
            const endpoint = this.SERVICES_ENDPOINT + this.JOB_REQUIREMENTS_ENDPOINT
                                + '/save?'
                                + 'jobId=' + jobId
                                + '&requirement=' + requirement;

            this.post(endpoint);
        },
        getApplicationsForJobPost: function (jobId) {
            const endpoint = this.SERVICES_ENDPOINT + this.JOB_POST_ENDPOINT
                            +'/applications?jobId=' + jobId;

            return this.get(endpoint);
        },
        saveApplicationWithIdAndJobPost: function (id, jobPostId) {
            const endpoint = this.SERVICES_ENDPOINT + this.APPLICATION_ENDPOINT + '/save' +
                '?userId=' + id + '&jobPostId=' + jobPostId;

            return new Promise((resolve, reject) => {
                Vue.axios.post(endpoint).then(response => {
                    resolve(response)
                }).catch(error => {
                    console.error(error);
                    resolve(error);
                })
            })
        },
        getCandidateProfileDto: function (id) {
            const endpoint = this.SERVICES_ENDPOINT + this.LINKED_IN_CONTROLLER_ENDPOINT
                            + this.GET_CANDIDATE_PROFILE_DTO_ENDPOINT + '?id=' + id;

            return this.get(endpoint);
        },
        getLinkedInSkills: function (id) {
            const endpoint = this.SERVICES_ENDPOINT + this.LINKED_IN_CONTROLLER_ENDPOINT
                            + '/' + id + '/get-skills';

            return this.get(endpoint)
        },
        submitLinkedInSkills: function (candidateId, skills) {
            const endpoint = this.SERVICES_ENDPOINT + this.LINKED_IN_CONTROLLER_ENDPOINT
                                + '/' + candidateId + '/save-skills';

            return this.postWithData(endpoint, skills);
        },
        updateApplicationStatus: function (applicationId, status) {
            const endpoint = this.SERVICES_ENDPOINT + this.APPLICATION_ENDPOINT + '/update-status'
                            + '?id=' + applicationId + '&applicationStatus=' + status;

            return this.put(endpoint);
        },
        authenticateHrExpert: function (username, password) {
            const endpoint = this.SERVICES_ENDPOINT + this.HR_EXPERT_ENDPOINT
                                + '/authenticate?' + 'name=' + username
                                + '&password=' + password;

            return new Promise ((resolve, reject) => {
                Vue.axios.post(endpoint).then(response => {
                    resolve(response)
                }).catch(error => {
                    console.error(error);
                    reject(error);
                })
            })
        },
        getCandidateApplications: function (linkedInId) {
            const endpoint = this.SERVICES_ENDPOINT + this.APPLICATION_ENDPOINT + '/candidate-applications' +
                                '?linkedInId=' + linkedInId;

            return this.get(endpoint);
        },
        getCandidateBlacklistStatus: function (candidateId) {
            const endpoint = this.SERVICES_ENDPOINT + this.LINKED_IN_CONTROLLER_ENDPOINT
                                + "/is-blacklisted?" + "candidateId=" + candidateId;

            return this.get(endpoint);
        },
        blacklistUser: function (candidateId, reason) {
            const endpoint = this.SERVICES_ENDPOINT + this.LINKED_IN_CONTROLLER_ENDPOINT
                            + "/blacklist?candidateId="
                            + candidateId + "&reason=" + reason;

            return this.post(endpoint);
        },
        performFreeTextSearch: function (query) {
            const endpoint = this.SERVICES_ENDPOINT + this.SEARCH_ENDPOINT
                              + '?query=' + query;

            return this.get(endpoint);
        },
        get: function (endpoint) {
            return new Promise ((resolve, reject) => {
                Vue.axios.get(endpoint).then(response => {
                    resolve(response.data)
                }).catch(error => {
                    console.error(error);
                    reject(error);
                })
            })
        },
        put: function (endpoint) {
            return new Promise ((resolve, reject) => {
                Vue.axios.put(endpoint).then(response => {
                    resolve(response.data)
                }).catch(error => {
                    console.error(error);
                    reject(error);
                })
            })
        },
        post: function (endpoint) {
            return new Promise ((resolve, reject) => {
                Vue.axios.post(endpoint).then(() => {
                    resolve()
                }).catch(error => {
                    console.error(error);
                    reject(error);
                })
            })
        },
        postWithData: function (endpoint, data) {
            return new Promise ((resolve, reject) => {
                Vue.axios.post(endpoint, data).then(() => {
                    resolve()
                }).catch(error => {
                    console.error(error);
                    reject(error);
                })
            })
        },
        delete: function(endpoint) {
            return new Promise ((resolve, reject) => {
                Vue.axios.delete(endpoint).then(() => {
                    resolve()
                }).catch(error => {
                    console.log(error);
                    reject(error);
                })
            })
        }
    }
}
