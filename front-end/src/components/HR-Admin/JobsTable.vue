<template>
<div class="table-responsive">
    <div class="card">
        <table class="table table-hover" v-if="dataFetched">
            <thead class="thead-light">
            <tr>
                <th scope="col">#</th>
                <th scope="col">Title</th>
                <th scope="col">Activation Date</th>
                <th scope="col">Expiration Date</th>
                <th scope="col">Number of Applications</th>
                <th scope="col">Edit</th>
                <th scope="col">See applications</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="(job, i) in jobs">
                <th scope="row">{{i + 1}}</th>
                <td>{{job.title}}</td>
                <td class="date">{{job.activationDate}}</td>
                <td class="date">{{job.expirationDate}}</td>
                <td class="applications">{{job.numberOfApplications}}</td>
                <td class="less-left-padding">
                    <button type="button" class="btn btn-info">
                        <router-link :to="getJobDetailsForPostWithId(jobs[i].id)" class="redirect">Edit</router-link>
                    </button>
                </td>
                <td class="less-left-padding">
                    <button type="button" class="btn btn-info">
                        <router-link :to="getApplicationsURLForPostWithId(jobs[i].id)" class="redirect">See applications</router-link>
                    </button>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
</template>

<script>
    import ServicesMixin from "../mixins/services-mixin";

    export default {
        name: "JobsTable",
        mixins: [ServicesMixin],

        data () {
            return {
                jobs: [],
                dataFetched: false
            }
        },
        methods: {
            getApplicationsURLForPostWithId: function (jobId) {
                return '/applications/' + jobId;
            },
            getJobDetailsForPostWithId: function (jobId) {
                return '/job-details/' + jobId + '/hr';
            }
        },
        mounted() {
            this.getHrExpertDtoList().then(HrAdminDtoList => {
                this.jobs = HrAdminDtoList;
            });

            this.dataFetched = true;
        }
    }
</script>

<style scoped lang="scss">
    @import "../../styles/components/_buttons.scss";
    @import "../../styles/abstracts/colors.scss";
    @import "../../styles/abstracts/typography.scss";

    * {
        @include set_font_opensans
    }

    .table-responsive .table {
        margin-bottom: 0;

        .btn {
            .btn-info, a {
                color: white;
            }
        }
    }

    .table th, .table td {
        vertical-align: middle;
    }

    td {
        p {
            vertical-align: middle;
        }
    }

    thead {
        border-radius: 200px;
    }

    .redirect {
        @include button_link_decoration_reset();
        color: $slightly-blue-gray;
    }

    .less-left-padding {
        padding-left: 6px;
    }

    .applications {
        padding-left: 0;
        padding-right: 15px;
        text-align: center;
    }

    .date {
        text-align: center;
        padding-right: 30px;
    }
</style>
