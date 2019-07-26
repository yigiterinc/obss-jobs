<template>
    <div class="job-card">
        <br>
        <b-card :title="jobPost.title"
                :img-src="jobPost.imageSrc"
                img-height="200"
                img-width="200"
                img-alt="Image"
                img-top
                tag="article"
                style="max-width: 20rem;"
                class="mb-2 card">
            <b-card-text class="job-card__description">{{jobPost.description}}</b-card-text>

            <div class="job-card__bottom-container">
                <span class="job-card__bottom-container__dates">
                    <p><em>Activation date: {{ this.getDDMMYYFormatFromYYYYMMDD(jobPost.activationDate) }}</em></p>
                    <p><em>Expiration date: {{ this.getDDMMYYFormatFromYYYYMMDD(jobPost.expirationDate) }}</em></p>
                </span>

                <button type="button" class="btn btn-danger job-card__bottom-container__information-button">
                    <router-link class="link" :to="getJobPostURL()">Learn more</router-link>
                </button>
            </div>
        </b-card>
    </div>
</template>
<script>
    import CommonsMixin from '../mixins/commons-mixin';

    export default {
        name: 'Job-Card',
        props: ['jobDetails', 'jobPost'],
        mixins: [CommonsMixin],
        jobPostURL: "",
        methods: {
            getJobPostURL: function() {
                return "/job-details/" + this.jobPost.id + "/us";
            }
        }
    }
</script>

<style lang="scss">
    @import "../../styles/abstracts/colors.scss";
    @import "../../styles/abstracts/typography.scss";

    .card {
        text-align: center;
        @include set_font_montserrat
    }

    .job-card__bottom-container__dates {
        position: absolute;
        left: 0;

        & p {
            margin-bottom: 0;
            margin-top: 2px;
            margin-left: 3px;
            font-size: 12px;
        }
    }

    .apply-button {
        margin-top: 1rem;
        border-radius: 200px;
        letter-spacing: 1.2px;
        text-transform: uppercase;
        padding: 0.8rem 2rem;
        color: $slightly-blue-gray;
    }

    .job-card__description {
        text-align: left;
    }

    .card-body {
        height: 15rem;
        position: relative;
    }

    .job-card__bottom-container {
        position: absolute;
        height: 2.25rem;
        width: 300px;
        bottom: 10px;
        right: 1rem;
    }

    .job-card__bottom-container__information-button {
        position: absolute;
        right: 0;
    }

    .link {
        @include remove_link_styles
    }
</style>
