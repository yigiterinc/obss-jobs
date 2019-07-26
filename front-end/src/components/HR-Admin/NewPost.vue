<template>
<div class="new-post">
    <Navbar></Navbar>
    <br><br>
    <div class="container title">
        <h1>Create a job post</h1>
        <hr>
        <br>
    </div>
    <form class="container">
        <div class="form-group">
            <label for="title">Job Title</label>
            <input type="text" class="form-control" id="title" placeholder="Enter title" v-model="title" required>
        </div>
        <div class="form-group">
            <label for="location">Location</label>
            <input type="text" class="form-control" id="location" placeholder="Enter location" v-model="location" required>
        </div>
        <div class="form-group">
            <label for="image-source">Post header image</label>
            <input type="url" class="form-control"
                   id="image-source" aria-describedby="image-help"
                   placeholder="Enter url" v-model="imageSource">
            <small id="image-help" class="form-text text-muted">Image to display in job post</small>
        </div>

        <label for="job-description">Job Description</label>
        <textarea class="form-control" id="job-description" rows="3" v-model="description"></textarea>

        <div class="requirements">
            <br>
            <h1 class="requirements__title">Job Requirements</h1>
            <hr>
            <br>
            <p>Enter the desired skills for the job, if you want more requirements,
                feel free to click add requirements button</p>
            <div class="form-group" v-for="(requirement, i) in requirements">
                <br>
                <input type="text" class="form-control" placeholder="Enter requirement" v-model="requirements[i]" required>
            </div>
            <div class="options">
                <button class="btn btn-danger add-button" v-on:click="addRequirement()">Add requirement</button>
            </div>
        </div>
    </form>
    <div>
    <div class="bottom-container container">
        <div class="date-pickers">
            <div class="activate">
                <input type="checkbox" v-model="activateWhenSubmitted"
                       class="form-check-input" id="activate" checked>
                <label class="form-check-label" for="activate">Activate when submitted</label>
            </div>
            <div class="expire-date">
                <label for="expire-date">Expire Date</label>
                <input type="date" name="expiredate" id="expire-date" v-model="expireDate">
                <button class="btn btn-primary submit-button" v-on:click="submitJobPost()">Submit</button>
            </div>
            <div class="activation-date" v-if="!activateWhenSubmitted">
                <label for="activation-date">Activation Date</label>
                <input type="date" name="activationdate" id="activation-date" v-model="activationDate">
            </div>
        </div>
    </div>

    </div>
</div>
</template>

<script>
    import Navbar from "../Nav.vue";
    import ServicesMixin from "../mixins/services-mixin";
    import CommonsMixin from "../mixins/commons-mixin";

    export default {
        name: "New-Post",
        mixins: [ServicesMixin, CommonsMixin],
        components: { Navbar },
        data() {
            return {
                title: "",
                location: "",
                imageSource: "",
                description: "",
                activateWhenSubmitted: true,
                activationDate: "",
                expireDate: "",
                requirements: Array(1)
            }
        },
        methods: {
            submitJobPost: function () {
                let success = true;

                if (this.isFormReadyToSubmit()) {
                    const expirationDateFormatted = this.getDateFormatted(this.expireDate);
                    const activationDate = this.activateWhenSubmitted ? this.getCurrentDate() : this.getDateFormatted
                    (this.convertStringToParseableDate(this.activationDate));
                    const urlifiedTitle = this.urlifyString(this.title);
                    let urlifiedDescription = this.urlifyString(this.description);

                    const createdBy = this.urlifyString(this.$store.state.hrExpertName);

                    if (createdBy != "") {

                        this.urlifyRequirements();

                        try {
                            this.saveJobPost(urlifiedTitle, this.location, this.activateWhenSubmitted,
                                createdBy, this.imageSource, urlifiedDescription, activationDate, expirationDateFormatted)
                                .then(response => {
                                    console.log(response.data);
                                    this.requirements.forEach(requirement => {
                                        try {
                                            this.saveRequirementForJobPost(response.data, requirement);
                                        } catch (e) {
                                            console.error(e);
                                            success = false;
                                        }
                                    });

                                })
                        } catch (e) {
                            console.error(e);
                            success = false;
                        }
                    } else {
                        this.showEmptyFieldsAlert();
                    }

                    if (success) {
                        this.showAuthorizedAlert()
                    } else {
                        this.showFailureAlert()
                    }
                }
            },
            getDateFormatted: function (date) {  // @Param: Date in YYYY-MM-DD format and returns in DD-MM-YYYY format
                if (typeof date !== "string")
                    date = date.toString();

                const day = date.split("-")[2];
                const month = date.split("-")[1];
                const year = date.split("-")[0];

                return day + "/" + month + "/" + year;
            },
            getCurrentDate: function () {
                const d = new Date();
                let day = d.getDay().toString();
                let month = d.getMonth().toString();
                let year = d.getFullYear().toString();

                if (parseInt(day) < 10) {
                    day = "0" + day;
                }

                if (parseInt(month) < 10) {
                    month = "0" + month;
                }

                return day + '/' + month + '/' + year;
            },
            isFormReadyToSubmit: function () {
                return this.areAllFieldsFilled() && this.isValidURL(this.imageSource);
            },
            showAuthorizedAlert: function () {
                this.$swal('Saved', 'Job post is successfully saved', 'success');
            },
            showEmptyFieldsAlert: function () {
                this.$swal('Error', 'Please fill the fields before you submit,' +
                    ' all fields must be filled with appropriate input' , 'error');
            },
            showFailureAlert: function () {
                this.$swal('Error', 'An error occurred' , 'error');
            },
            areAllFieldsFilled: function () {
                for (let i = 0; i < this.requirements.length; i++) {
                    if (!this.notEmptyString(this.requirements[i])) // is empty
                        return false;
                }

                return this.notEmptyString(this.title) && this.notEmptyString(this.description)
                        && this.notEmptyString(this.expireDate.toString());
            },
            notEmptyString: function (str) {
                return !(str.length === 0 || !str.trim());
            },
            isValidURL: function (url) {
                let a = document.createElement('a');
                a.href = url;
                return (a.host && a.host != window.location.host);
            },
            addRequirement: function () {
                this.requirements.push("");
            },
            urlifyRequirements: function () {
                for (let i = 0; i < this.requirements.length; i++) {
                    this.requirements[i] = this.urlifyString(this.requirements[i]);
                }
            }
        }
    }
</script>

<style scoped lang="scss">
    @import '../../styles/abstracts/colors';
    @import '../../styles/abstracts/typography';

    * {
        @include set_font_opensans
    }

    .new-post {
        color: $slightly-blue-black;
    }

    form {
        position: relative;
    }

    .title {
        text-align: center;

        h1 {
            font-weight: 300;
            font-size: 3rem;
        }
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

    .activation-date {
        float: right;
        margin-bottom: 1rem;
    }

    #activation-date {
        margin-right: 20px;
        margin-top: 7px;
    }

    .expire-date {
        float: right;
        margin-bottom: 1rem;
    }

    #expire-date {
        margin-right: 20px;
    }

    .btn-primary {
        background-color: $color-primary-1;
        border: 0;

        &:hover {
            background-color: darken($color-primary-1, 10%);
        }
    }

    .activate {
        margin-left: 58.5vw;
        margin-top: 2rem;
        margin-bottom: 1rem;
    }

    .requirements__title {
        margin-top: 1rem;
        text-align: center;
        font-weight: 300;
        font-size: 2.5rem;
    }

    .submit-button {
        margin-bottom: 5px;
        padding: 10px;
    }

    .options {
        float: left;
    }

    label {
        display: block;
    }

    .add-button {
        margin-top: 0.5rem;
        margin-bottom: 3rem;
    }

    .date-pickers {
        margin-top: 5rem;
    }

    input[type="date"] {
        border: 1px solid #c4c4c4;
        border-radius: 5px;
        background-color: #fff;
        padding: 3px 5px;
        box-shadow: inset 0 3px 6px rgba(0,0,0,0.1);
        width: 190px;
    }

    input[type="text"], input[type="url"] {      // Override bootstrap width
        width: 77vw !important;
    }
</style>
