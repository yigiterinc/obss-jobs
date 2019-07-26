<template>
<div class="nav navbar-dark bg-light">
    <div>
        <nav class="navbar">
            <div class="user">

            </div>
            <router-link to="/"><img src="../assets/images/obss-logo.png" alt="obss-logo" class="nav-bar__logo"></router-link>
            <div class="nav-bar__my-profile" v-if="userAuthenticated">
                <router-link class="nav-bar__link" :to="getProfileUrl()">My Profile</router-link>
                <p class="nav-bar__link logout" @click="logOut()">Log out</p>
            </div>
            <div class="nav-bar__hr" v-if="!userAuthenticated && hrExpertAuthenticated">
                <router-link class="nav-bar__link" to="/hr-expert">HR Panel</router-link>
                <p class="nav-bar__link" @click="logOut">Log out</p>
            </div>
        </nav>
    </div>
    <hr>
</div>
</template>

<script>
    export default {
        name: "Nav",
        computed: {
            userAuthenticated() {
                return this.$store.getters.userAuthenticated;
            },
            hrExpertAuthenticated() {
                return this.$store.getters.hrExpertAuthenticated;
            },
        },
        methods: {
            getProfileUrl() {
                return '/profile/' + this.$store.state.userId;
            },
            logOut() {
                console.log('here')
                if (this.hrExpertAuthenticated) {
                    this.$store.commit('setHrExpertName', '');
                } else if (this.userAuthenticated) {
                    this.$store.commit('setUserId', '');
                }

                this.$swal("Log out succesful", "You are succesfully logged out, redirecting to home page", "info");
                setTimeout(() => this.$router.push("/"), 2000);
            }
        }
    }
</script>

<style scoped lang="scss">
    @import '../styles/abstracts/colors';

    .nav {
        height: 4rem;
        position: relative;
        border: none;
        border: 0;
    }

    .nav-bar__logo {
        top: 70%;
        height: 40px;
        margin-left: 4rem;
        position: absolute;
    }

    .nav-bar__my-profile {
        position: absolute;
        left: 85vw;    // TODO: Refactor this
        margin-top: 1.5rem;
    }

    .nav-bar__hr {
        position: absolute;
        left: 85vw;    // TODO: Refactor this
        margin-top: 1.5rem;
    }

    .nav-bar__link {
        text-decoration: none;
        display: inline;
        margin-right: 20px;
        font-size: 15px;
        color: $slightly-blue-gray;
        white-space: nowrap;
        font-family: 'Open Sans', Arial, sans serif;

        &:hover {
            color: $color-primary-2;
        }
    }

    .logout {
        cursor: pointer;

        &:hover, &:visited {
            cursor: pointer;
        }
    }
</style>
