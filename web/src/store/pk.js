import ModuleUser from './user'
export default {
    state: {
        status: "matching", // matching, playing
        socket: null,
        opponent_name: "",
        opponenet_photo: "",
        gamemap: null,
    },
    getters: {

    },
    mutations: {
        updateSocket(state, socket) {
            state.socket = socket ;
        },
        updateOpponent(state, opponent) {
            state.opponent_name = opponent.username ;
            state.opponent_photo = opponent.photo ;
        },
        updateStatus(state, status) {
            state.status = status ;
        },
        updateGamemap(state, gamemap) {
            state.gamemap = gamemap ;
        } 
    },
    actions: {
    },
    modules: {
        user: ModuleUser,
    }
}