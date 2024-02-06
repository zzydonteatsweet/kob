<template>
    <ContentField>
        BotInfo
    </ContentField>
</template>

<script>
import ContentField from '../../../components/ContentField.vue'
import $ from 'jquery'
import { useStore } from 'vuex';

export default {
    components: {
        ContentField
    },
    setup() {
        console.log("In") ;
        const store = useStore() ;
        // $.ajax({
        //     url: "http://127.0.0.1:3000/user/bot/add/",
        //     type: "post",
        //     data: {
        //         name: "测试bot6",
        //         description: "测试描述6",
        //         content: "测试代码",

        //     },
        //     headers: {
        //         Authorization: "Bearer " + store.state.user.token,
        //     },
        //     success(resp) {
        //         console.log(resp) ;
        //     },
        //     error(resp) {
        //         console.log(resp) ;
        //     }
        // })
        // $.ajax({
        //     url: "http://127.0.0.1:3000/user/bot/getlist/",
        //     type: "get",
        //     headers: {
        //         Authorization: "Bearer " + store.state.user.token,
        //     },
        //     success(resp) {
        //         console.log(resp) ;
        //     },
        //     error(resp) {
        //         console.log(resp) ;
        //     }
        // })
        // $.ajax({
        //     url: "http://127.0.0.1:3000/user/bot/remove/",
        //     type: "post",
        //     data: {
        //         bot_id: 6, 
        //     },
        //     headers: {
        //         Authorization: "Bearer " + store.state.user.token,
        //     },
        //     success(resp) {
        //         console.log(resp) ;
        //     },
        //     error() {
        //         console.log("failed") ;
        //     }
        // })
        $.ajax({
            url: "http://127.0.0.1:3000/user/bot/update/",
            type: "post",
            data: {
                bot_id: 4, 
                name: "测试bot改4",
                description: "更改检查",
                content: "测试代码2",
            },
            headers: {
                Authorization: "Bearer " + store.state.user.token,
            },
            success(resp) {
                console.log(resp) ;
            },
            error() {
                console.log("failed") ;
            }
        })


    }
}
</script>

<style scoped>

</style>