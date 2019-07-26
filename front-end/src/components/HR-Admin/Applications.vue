<template>
    <div class="applications">
        <Navbar></Navbar>
        <div class="container">
            <div class="description">
                <h1 class="title">Applications</h1>
                <p class="information">You can search for a skill by using the search bar. Use the dropdown to filter by application status
                    and click the arrow button to sort by candidate compatibility. Proceed status button takes the application to
                    the next status (Awaiting -> Under review -> Interview -> Job Offer -> Accepted) </p>
                <label for="search">Search for a word</label>
            </div>

            <form class="form-inline search-form">
                <div class="form-group">
                    <input type="text" class="form-control search" id="search"
                           placeholder="Search" v-model="searchedWord"
                           @input="searchTable()" required>
                </div>
                <multiselect v-model="statusFilter" :options="applicationStatuses" class="multiselect" @input="filterByStatus()"></multiselect>
            </form>
            <div class="table-responsive">
                <div class="card">
                    <table class="table table-hover">
                        <thead class="thead-light">
                        <tr>
                            <th scope="col">#</th>
                            <th scope="col" v-for="(col, i) in tableData.columns" @click="sortByColumn(i)">
                                {{col.label}}
                            </th>
                        </tr>
                        </thead>
                        <tbody id="table">
                        <tr v-for="(row, i) in tableData.rows">
                            <th>{{i + 1}}</th>
                            <td>
                                <router-link class="candidate-name"
                                             :to="getProfileUrl(row.candidateId)">{{row.name}}
                                </router-link>
                            </td>
                            <td>{{row.date}}</td>
                            <td>{{row.compatibility}}</td>
                            <td>{{row.status}}</td>
                            <td>
                                <button class="btn btn-success proceed-button"
                                        @click="proceedApplicationStatus(i)">
                                    Proceed Status
                                </button>
                            </td>
                            <td>
                                <button class="btn btn-danger reject-button"
                                        @click="rejectApplication(i)">
                                    Decline Application
                                </button>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>

        </div>
    </div>
</template>

<script>
    import Navbar from "../Nav"
    import Multiselect from 'vue-multiselect'

    import JQuery from 'jquery'
    let $ = JQuery;

    import ServicesMixin from "../mixins/services-mixin";
    import CommonsMixin from "../mixins/commons-mixin";

    export default {
        name: "Applications",
        mixins: [ServicesMixin, CommonsMixin],
        components: {
            Navbar,
            Multiselect,
        },
        data () {
            return {
                statusFilter: null,
                searchedWord: '',
                jobRequirements: [],
                applications: [],
                filteredByStatus: false,
                applicationStatuses: ['Awaiting', 'Under review', 'Interview', 'Job Offer', 'Accepted'],
                tableData: {
                    columns: [
                        {
                            label: 'Name',
                            field: 'name',
                        },
                        {
                            label: 'Date',
                            field: 'date',
                        },
                        {
                            label: 'Compatibility',
                            field: 'compatibility',
                        },
                        {
                            label: 'Status',
                            field: 'status',
                        },
                        {
                            label: 'Proceed Application Status',
                            field: 'changeStatus',
                        },
                        {
                            label: 'Decline application',
                            field: 'declineApplication'
                        }
                    ],
                    rows: []
                }
            }
        },
        methods: {
            getJobId: function() {
                const url = window.location.pathname;
                // return the first url param
                return url.split("/")[2];
            },
            getProfileUrl: function (id) {
                return '/profile/' + id;
            },
            getApplications() {
                const jobId = this.getJobId();
                this.getApplicationsForJobPost(jobId).then(response => {
                    this.applications = response;
                    this.fillTable(response);
                })
            },
            fillTable(applications) {
                let statusButtonId = 0;

                applications.forEach(application => {
                    this.tableData.rows.push({
                        name: application.candidateLinkedIn.fullName,
                        candidateId: application.candidateLinkedIn.id,
                        date: this.parseDateFromDateObject(application.applicationDate),
                        compatibility: this.calculateJobCompatibility(application.candidateLinkedIn.linkedInSkillSet),
                        status: application.applicationStatus,
                        candidateSkillSet: application.candidateLinkedIn.linkedInSkillSet
                    });

                    statusButtonId++;
                });
            },
            calculateJobCompatibility (skillSet) {
                let score = 0;

                this.jobRequirements.forEach(requirement => {
                    const wordsInRequirement = requirement.split(" ");
                    wordsInRequirement.forEach(word => {
                        skillSet.forEach(skillObject => {
                            if (skillObject.skill.toString().toLowerCase() === word.toLowerCase()) {
                                score++;
                            }
                        })
                    });
                });

                const numberOfRequirements = this.jobRequirements.length;
                return Math.floor((score / numberOfRequirements) * 100) + "%";
            },
            proceedApplicationStatus (index) {
                const application = this.applications[index];
                const applicationId = application.id;
                const applicationStatus = application.applicationStatus;

                let nextApplicationStatus = "";

                for (let i = 0; i < this.applicationStatuses.length - 1; i++) {
                    if (this.applicationStatuses[i] === applicationStatus) {
                        nextApplicationStatus = this.applicationStatuses[i + 1];
                    }
                }

                const nextApplicationStatusUrlified = this.urlifyString(nextApplicationStatus);

                this.updateApplicationStatus(applicationId, nextApplicationStatusUrlified)
                    .then(() => {
                        this.showSuccessfulUpdateAlert();
                        this.getApplications();
                    }).catch(error => {
                        console.error(error);
                        this.showUpdateFailureAlert();
                })
            },
            showSuccessfulUpdateAlert: function () {
                this.$swal('Success', 'Application status is succesfully updated' , 'success');
            },
            showUpdateFailureAlert: function () {
                this.$swal('Error', 'An error occurred while updating the application status' , 'error');
            },
            rejectApplication: function (index) {
                const application = this.applications[index];
                const applicationId = application.id;

                this.updateApplicationStatus(applicationId, "Declined")
                    .then(() => {
                        this.getApplications()
                    })
            },
            filterByStatus() {
                if (this.filteredByStatus) {
                    return this.resetFilter();
                } else {
                    let filter = this.statusFilter.toUpperCase();

                    let table = document.getElementsByClassName("table")[0];
                    let tr = table.getElementsByTagName("tr");

                    for (let i = 0; i < tr.length; i++) {
                        let td = tr[i].getElementsByTagName("td")[3];
                        if (td) {
                            let txtValue = td.textContent || td.innerText;

                            if (txtValue.toUpperCase().indexOf(filter) > -1) {
                                tr[i].style.display = "";
                            } else {
                                tr[i].style.display = "none";
                            }
                        }
                    }

                    this.filteredByStatus = true;
                }
            },
            resetFilter () {
                let table = document.getElementsByClassName("table")[0];
                let tr = table.getElementsByTagName("tr");

                for (let i = 0; i < tr.length; i++) {
                    let td = tr[i].getElementsByTagName("td")[3];
                    if (td) {
                        tr[i].style.display = "";   // make all tds visible
                    }
                }

                this.filteredByStatus = false;
            },
            sortByColumn(columnNumber) {
                let first, second, numberOfSwitches, i = 0;

                let table = document.getElementsByClassName("table")[0];
                let switching = true;
                let shouldSwitch = false;
                let dir = "asc";

                while (switching) {
                    switching = false;
                    var rows = table.getElementsByTagName("tr");

                    for (i = 1; i < (rows.length - 1); i++) {
                        // Start by saying there should be no switching:
                        shouldSwitch = false;
                        first = rows[i].getElementsByTagName("TD")[columnNumber];
                        second = rows[i + 1].getElementsByTagName("TD")[columnNumber];
                        if (dir === "asc") {
                            if (first.innerHTML.toLowerCase() > second.innerHTML.toLowerCase()) {
                                // If so, mark as a switch and break the loop:
                                shouldSwitch = true;
                                break;
                            }
                        } else if (dir === "desc") {
                            if (first.innerHTML.toLowerCase() < second.innerHTML.toLowerCase()) {
                                shouldSwitch = true;
                                break;
                            }
                        }
                    }
                    if (shouldSwitch) {
                        rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
                        switching = true;
                        numberOfSwitches ++;
                    } else {
                        if (numberOfSwitches === 0 && dir === "asc") {
                            dir = "desc";
                            switching = true;
                        }
                    }
                }
            },
            searchTable() {
                $(document).ready(() => {
                    $("#search").on("keyup", function() {
                        let value = $(this).val().toLowerCase();
                        $("#table tr").filter(function() {
                            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
                        });
                    });
                });
            },
            fetchRequirements(jobId) {
                this.getJobRequirements(jobId).then(jobRequirements => {
                    jobRequirements.forEach(jobRequirementsDto => {
                        this.jobRequirements.push(jobRequirementsDto.requirement)
                    })
                });
            }
        },
        mounted () {
            if (!this.$store.getters.hrExpertAuthenticated) {
                this.$router.push("/");
            }

            this.fetchRequirements(this.getJobId());
            this.getApplications();
        }
    }
</script>

<style src="vue-multiselect/dist/vue-multiselect.min.css"></style>

<style scoped lang="scss">
    @import "../../styles/abstracts/typography";

    * {
        @include set_font_opensans();

        a, button {
            text-decoration: none;
            color: $slightly-blue-white
        }
    }

    .applications {
        height: 100vh;
    }

    .title {
        margin-top: 2rem;
        margin-bottom: 2rem;
        text-align: center;
    }

    .search {
        width: 40vw;
    }

    .multiselect {
        margin-left: 10px;
        width: 12vw;
    }

    .search-form {
        margin-bottom: 2rem;
    }

    .information {
        margin-bottom: 3rem;
        font-size: 17px;
    }

    .proceed-button {
        margin-left: 20px;
    }

    table {
        margin-bottom: 0;

        .candidate-name {
            color: $slightly-blue-black;

            &:hover {
                color: #0079B3;
            }
        }

        td {
            color: $slightly-blue-black;
        }
    }
</style>
