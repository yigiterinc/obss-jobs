<template>
    <div class="content">
        <h3><strong>{{ title }}</strong></h3>
        <p class="content--motto"><em><strong>{{ motto }}</strong></em></p>
        <p>{{ description }}</p>
    </div>
</template>
<script>
    export default {
        name: 'Information',
        props: ['title', 'motto', 'description']
    }
</script>
<style lang="scss">
    @import '../../styles/abstracts/typography';
    @import '../../styles/abstracts/colors';
    @import '../../styles/abstracts/resets';

    .content {
        @include set_font_opensans;

        margin-left: 6rem;
        margin-right: 6rem;

        h3 {
            text-transform: uppercase;
            color: $slightly-blue-gray;
        }

        .content {
            p:nth-child(3) {
                font-size: 15px;
                color: rgb(121, 106, 91);
                font-weight: 300;
                margin-bottom: 5rem;
            }
        }

        .content--motto {
            margin-top: 0;
            margin-bottom: 2rem;
            font-size: 1.15rem;
        }
    }

</style>
