<template>
<div class="job-posts" v-if="dataFetched">
    <Nav :is-candidate="true" :has-logged-in="true"></Nav>
    <div class="container">
        <b-row class="row-container" v-for="row in NUMBER_OF_ROWS">
            <b-col v-for="col in NUMBER_OF_COLS">
                <job-card class="j-card" :job-post="jobPosts[getJobPostIndex(row, col)]"></job-card>
            </b-col>
        </b-row>
    </div>
    <br><br>
</div>
</template>

<script>
    import Nav from "../Nav.vue";
    import JobCard from "./JobCard";
    import ServicesMixin from "../mixins/services-mixin";

    export default {
        name: "JobPosts",
        components: {JobCard, Nav},
        mixins: [ServicesMixin],
        data () {
            return {
                jobPosts: null,
                NUMBER_OF_COLS: 3,
                NUMBER_OF_ROWS: 0,
                dataFetched: false
            }
        },
        methods: {
            getJobPostIndex: function (row, col) {
                return (row - 1) * 3 + (col - 1);
            }
        },
        mounted() {
            this.getJobPosts().then(jobPosts => {
                this.jobPosts = jobPosts;
                this.dataFetched = true;
                this.NUMBER_OF_ROWS = Math.ceil(jobPosts.length / 3);
            }).catch(() => {
                console.log("An error occurred while getting job posts");
            });
        }
    }
</script>

<style lang="scss">
@import "../../styles/abstracts/colors";
@import "../../styles/abstracts/typography";

.row-container {
    display: flex;
}

.j-card {
    height: 100%;
}
</style>

