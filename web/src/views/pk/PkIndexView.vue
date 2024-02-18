<template>
    <PlayGround v-if="$store.state.pk.status === 'playing' "/>
    <MatchGround v-if="$store.state.pk.status === 'matching'"/>
    <ResultBoard v-if="$store.state.pk.loser != 'none'"/>
</template>

<script>
    import PlayGround from '@/components/PlayGround.vue';
    import MatchGround from '@/components/MatchGround.vue' ;
    import ResultBoard from '@/components/ResultBoard.vue'
    import { onMounted, onUnmounted } from 'vue';
    import { useStore } from 'vuex';
    export default {
        name: "Pk_index",
        components: {
            PlayGround,
            MatchGround,
            ResultBoard,
        },
        setup() {
            const store = useStore() ;
            const socketUrl = `ws://127.0.0.1:3000/websocket/${store.state.user.token}/` ;

            let socket = null ;
            onMounted(() => {
                socket = new WebSocket(socketUrl) ; 
                store.commit("updateOpponent", {
                    username: "我的对手",
                    photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
                })
                socket.onopen = () => {
                    console.log("connected!") ;
                    store.commit("updateSocket", socket) ;
                }

                socket.onmessage = msg => {
                    const data = JSON.parse(msg.data) ;
                    if(data.event === "start-matching") {
                        store.commit("updateOpponent", {
                            username: data.opponent_name,
                            photo: data.opponent_photo, 
                        }) ;
                        setTimeout(() => {
                            store.commit("updateStatus", "playing") ;
                        }, 100) ;
                        store.commit("updateGame", data.game) ;
                    }else if(data.event === "move") {
                        const game = store.state.pk.gameObject ;
                        console.log(data.a_direction);
                        console.log(data.b_direction);
                        const [snake0, snake1] = game.snakes ;
                        snake0.set_direction(data.a_direction) ;
                        snake1.set_direction(data.b_direction) ;
                    }else if(data.event === "result") {
                        const game = store.state.pk.gameObject ;
                        const [snake0, snake1] = game.snakes ;

                        if(data.loser === "all" || data.loser === "a") {
                            snake0.status = "dead" ;
                        }
                        if(data.loser === "all" || data.loser === "b") {
                            snake1.status = "dead" ;
                        }
                        store.commit("updateLoser", data.loser) ;
                    }
                }

                socket.onclose = () => {
                    console.log("disconneted") ;
                }
            }) ;
            onUnmounted(() => {
                socket.close() ;
                store.commit("updateLoser", "none");
                store.commit("updateStatus", "matching") ;
            }) ;
        }

    }
</script>

<style scoped>

</style>