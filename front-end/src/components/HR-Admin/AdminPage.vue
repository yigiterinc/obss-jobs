<template>
    <div class="admin-page">
        <Navbar class="Nav"></Navbar>
        <div class="space"></div>
        <div class="container">
            <JobsTable class="jobs-table"></JobsTable>
            <div class="button-container">
                <button class="btn btn-info scout-button">
                    <router-link to="/talent-scout" class="redirect">Talent Scout</router-link>
                </button>
                <button class="btn btn-info new-ad">
                    <router-link to="/new-post" class="redirect">Post a new ad</router-link>
                </button>
            </div>
        </div>
    </div>
</template>

<script>
    import JobsTable from "./JobsTable.vue";
    import Navbar from "../Nav.vue"

    import ServicesMixin from "../mixins/services-mixin"

    export default {
        name: "AdminPage",
        components: {JobsTable, Navbar},
        mixins: [ServicesMixin],

        data () {
            return {
                jobs: [],
                dataFetched: false
            }
        },
        mounted() {
            this.getJobPosts().then(jobPosts => {
                this.jobs = jobPosts;
                this.dataFetched = true;
            }).catch(() => {
                console.log("An error occurred while getting job posts");
            });
        }
    }
</script>

<style scoped lang="scss">
    @import "../../styles/abstracts/typography.scss";
    @import "../../styles/abstracts/colors.scss";
    @import "../../styles/components/_buttons.scss";

    .admin-page {
        height: 100vh;

        .btn {
            .btn-info, a {
                color: white;
            }
        }
    }

    .space {
        height: 5rem;
    }

    .button-container {
        width: 69.25rem;
        position: relative;
    }

    .new-ad {
        top: 2rem;
        right: 2rem;
        position: absolute;
        @include set_font_opensans();
    }

    .scout-button {
        position: absolute;
        top: 2rem;
        right: 11rem;
    }

    .redirect {
        @include button_link_decoration_reset();
        @include remove_link_styles();

        color: $slightly-blue-gray;

        &:hover {
            color: $slightly-blue-gray;
        }
    }

</style>
