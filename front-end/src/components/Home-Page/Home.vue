<template>
  <div>
    <div class="vld-parent">
      <loading :active.sync="loginCompleted"
               :is-full-page="true">
      </loading>
    </div>
    <Nav :has-logged-in="true"></Nav>
    <header class="header">
      <div class="header__text-box">
        <h1 class="heading-primary">
          <span class="heading-primary--main">Join</span>
          <span class="heading-primary--sub">the team</span>
        </h1>

        <h3>Login with your <strong>LinkedIn</strong> account to apply for jobs</h3>

        <div class="home-button">
          <button v-if="!userAuthenticated" type="button" class="btn btn-light">
            <a :href="linkedInLogin" class="redirect">Login with LinkedIn</a>
          </button>
          <button type="button" class="btn btn-light">
            <router-link to="/job-posts" class="redirect">See job opportunities</router-link>
          </button>
        </div>
      </div>
    </header>

    <information v-for="information in information_data"
                 :title="information.title"
                 :motto="information.motto"
                 :description="information.description">
    </information>
  </div>
</template>

<script>
  import Nav from '../Nav.vue';
  import Information from "./Information";

  import Vue from 'vue'
  import ServicesMixin from "../mixins/services-mixin";

  import Loading from 'vue-loading-overlay';
  import 'vue-loading-overlay/dist/vue-loading.css';

  export default {
    name: 'home',
    mixins: ['ServicesMixin'],
    components: {
      Information,
      Nav,
      Loading
    },
    data() {
      return {
        loginCompleted: false,
        information_data: [{
          title: "Who We Hire",
          motto: "The insatiably curious rebels in geek gear",
          description: "Our value is our intellectual capital, which means that recruiting the brightest," +
                      " most curious, most passionate individuals is our lifeblood, our focus, and our recipe for success." +
                      " That’s why we’re fanatically focused on finding the best minds and providing them with a " +
                      "wonderful environment in which to grow, to explore and to be appreciated."
        },
        {
          title: "Where We Work",
          motto: "The environment nourishes the creative soul",
          description: "Environment impacts emotions and emotions impact everything — energy, focus and motivation are" +
                  " just the tip of the iceberg — and that’s why we care so much about building an office environment" +
                  " that nourishes the senses and invigorates the mind. It might seem unnecessary to some, but to us" +
                  " it’s part of the fuel mixture that makes OBSS the right home for the rare breed who are" +
                  " perpetually in cognitive overdrive."
        },
        {
          title: "What We Contribute",
          motto: "The conceptual engine of a better tomorrow",
          description: "What we take for granted today, much of which was mere fantasy in the past, was made practical " +
                  "by the march of progress delivered by the curious minds who refused to simply copy." +
                  " We refuse to copy too, because we’re driven to try our hardest to channel imagination into innovation." +
                  " And that’s why we’re investing in robotics, autonomous vehicles, clean energy, advanced software " +
                  "development tools and process management technologies among other stuff we find addictively cool."
        }
      ],
        linkedInLogin: "https://www.linkedin.com/oauth/v2/authorization?response_type=code&client_id=86uobfdmclzprd" +
                "&redirect_uri=http://localhost:8080" +
                "&state=ASk220aSFAxx&scope=r_liteprofile%20r_emailaddress%20w_member_social"
      }
    },
    computed: {
      userAuthenticated: function () {
        return this.$store.getters.userAuthenticated
      }
    },
    methods: {
      isInAuthenticationProcess: function () {
        const url = window.location.search;
        // return the first url param
        if (url && url.split("?"))
          return url.split("?")[1].substr(0, 4) === 'code';

        return false;
      },
      exchangeLinkedInToken(code) {
        const endpoint = 'http://localhost:8888/api/services/controller' + '/linked-in' + '/exchange-token'
                + '?code=' + code;

        return new Promise ((resolve, reject) => {
          Vue.axios.post(endpoint).then((response) => {
            resolve(response)
          }).catch(error => {
            reject(error);
          })
        })
      }
    },
    mounted() {
      if (this.isInAuthenticationProcess() && !this.userAuthenticated) {
        this.loginCompleted = true;

        const url = window.location.search;

        let split = url.split('?code=')[1];
        const code = split.split('&state')[0];

        this.exchangeLinkedInToken(code).then(response => {
          this.$store.commit('setUserId', response.data);
          this.loginCompleted = false;

          this.$swal("Login successful", "You successfully logged in, adding skills to your profile will " +
                  "greatly increase your chances", "success");
        }).catch(error => {
          if (error.response && error.response.status === 409) {
            window.location.href = this.linkedInLogin;
            return;
          }

          console.error(error);
        });
      }
    }
  }
</script>

<style lang="scss">
  @import '../../styles/abstracts/typography';
  @import '../../styles/abstracts/colors';
  @import '../../styles/abstracts/resets';
  @import '../../styles/components/_buttons';

  .main-title {
    @include set_title_font;
  }

  .header {
    background-image: linear-gradient(
                    to right bottom,
                    rgba(49, 147, 213, 0.42),
                    rgba(50, 134, 213, 0.33)),
    url(../../assets/images/obss-banner.jpg);
    background-size: cover;
    background-position: top;
    height: 95vh;
    width: 100vw;
    position: relative;
    margin-bottom: 5rem;
    @include set_title_font;
  }

  .heading-primary {
    color: $slightly-blue-white;
    text-transform: uppercase;
    backface-visibility: hidden;
    margin-bottom: 3rem;
  }

  .heading-primary--main {
    display: block;
    font-size: 5rem;
    font-weight: 300;
    letter-spacing: 2rem;
    margin-left: 1.3rem;
    margin-bottom: 1.2rem;
    animation-name: move-in-left;
    animation-duration: 1.5s;
    animation-timing-function: ease-out;
  }

  .heading-primary--sub {
    display: block;
    font-size: 3rem;
    font-weight: 300;
    letter-spacing: 1.60rem;
    animation: move-in-right 1.5s ease-out;
  }

  .header__text-box {
    position: absolute;
    top: 40%;
    left: 50%;
    transform: translate(-50%, -50%);
    text-align: center;

    h3 {
      @include set_font_opensans;
      color: $slightly-blue-white;
      font-size: 1.2rem;
      font-weight: 300;
    }
  }

  .home-button {
    button {
      margin-top: 1rem;
      border-radius: 200px;
      letter-spacing: 1.2px;
      text-transform: uppercase;
      padding: 0.8rem 2rem;
      color: $slightly-blue-gray;

      &:hover {
        transform: translateY(-3px);
      }
    }

    button:nth-child(2) {
      margin-left: 2rem;
    }
  }

  .home-button button:nth-child(2) {
    margin-left: 2rem;
  }

  .redirect {
    @include button_link_decoration_reset();
    color: $slightly-blue-gray;

    &:hover {
      color: $slightly-blue-gray;
    }
  }

  @keyframes move-in-right {
    0% {
      opacity: 0;
      transform: translateX(10rem);
    }
    80% {
      transform: translateX(-1rem);
    }
    100% {
      opacity: 1;
      transform: translate(0);
    }
  }

  @keyframes move-in-left {
    0% {
      opacity: 0;
      transform: translateX(-10rem);
    }
    80% {
      transform: translateX(1rem);
    }
    100% {
      opacity: 1;
      transform: translate(0);
    }
  }

  @keyframes move-in-right {
    0% {
      opacity: 0;
      transform: translateX(10rem);
    }
    80% {
      transform: translateX(-1rem);
    }
    100% {
      opacity: 1;
      transform: translate(0);
    }
  }

  @keyframes move-in-left {
    0% {
      opacity: 0;
      transform: translateX(-10rem);
    }
    80% {
      transform: translateX(1rem);
    }
    100% {
      opacity: 1;
      transform: translate(0);
    }
  }
</style>

