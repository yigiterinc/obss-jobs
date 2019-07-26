<template>
    <div class="hr-login">
        <Navbar></Navbar>
        <div class="container hr-login__form">
            <form class="login-form">
                <div class="form-group">
                    <label for="fullName">Full Name</label>
                    <input type="text" class="form-control" id="fullName" v-model="fullName"
                           aria-describedby="fullNameHelp" placeholder="Enter your name">
                </div>
                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" class="form-control" id="password" v-model="password" placeholder="Password">
                </div>
            </form>
            <button class="btn btn-light hr-login__form__button"
                    @click="authenticate(fullName, password)">Login
            </button>
        </div>
    </div>
</template>

<script>
    import Navbar from '../Nav.vue'
    import ServicesMixin from "../mixins/services-mixin"
    import CommonsMixin from "../mixins/commons-mixin"

    export default {
        name: "HrExpert",
        mixins: [ServicesMixin, CommonsMixin],
        data () {
            return {
                fullName: "",
                password: ""
            }
        },
        components: {
            Navbar
        },
        methods: {
            authenticate: function (fullName, password) {
                const fullNameUrlified = this.urlifyString(fullName);

                this.authenticateHrExpert(fullNameUrlified, password)
                    .then(authenticationStatus => {
                        let authenticated = authenticationStatus.data;

                        if (authenticated) {
                            this.showAuthorizedAlert();
                            this.saveHrLoginStatusToStore();
                            this.redirectToHomePageAfterDataIsLoaded();
                        } else {
                            this.showUnauthorizedAlert()
                        }
                });
            },
            showAuthorizedAlert: function () {
                const welcomeMessage = 'Welcome, ' + this.fullName + ' redirecting to HR panel...';
                this.$swal('Please wait', welcomeMessage, 'success');
            },
            showUnauthorizedAlert: function () {
                this.$swal('Unauthorized', 'Invalid credentials!' , 'error');
            },
            saveHrLoginStatusToStore: function () {
                this.$store.commit("setHrExpertName", this.fullName);
            },
            redirectToHomePageAfterDataIsLoaded: function () {
                // Wait 3 seconds for data to be loaded into Vuex store
                setTimeout(() => this.$router.push("/hr-expert"), 3000);
            }
        }
    }
</script>

<style scoped lang="scss">
    @import '../../styles/abstracts/typography';
    @import '../../styles/abstracts/colors';

    .login-form {
        width: 20rem;

        label {
            color: $slightly-blue-white;
            font-size: 18px;
        }

        .text-muted {
            color: $slightly-blue-white !important;
        }
    }

    .hr-login {
        background-image: linear-gradient(
                        to right bottom,
                        rgba(49, 147, 213, 0.42),
                        rgba(50, 134, 213, 0.33)),
        url(../../assets/images/obss-banner.jpg);
        background-size: cover;
        background-position: top;
        height: 100vh;
        width: 100vw;
        position: relative;
        margin-bottom: 5rem;
        @include set_title_font;
    }

    .hr-login__form {
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-15%, -35%);
    }

    .hr-login__form__button {
        margin-left: 8rem;
    }
</style>
