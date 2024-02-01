<template>
    <ContentField>
        <div class="row justify-content-md-center">
            <div class="col-3">
                <form @submit.prevent="login">
                    <div class="mb-3">
                        <label for="username" class="form-label">Username</label>
                        <input v-model="username" type="text" class="form-control" id="exampleFormControlInput1" placeholder="Username">
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">Password</label>
                        <input v-model="password" type="password" class="form-control" id="exampleFormControlInput1" placeholder="Password">
                    </div>
                    <div class="error-message">{{ error_message }}</div>
                    <button type="submit" class="btn btn-secondary">submit</button>

                </form>
            </div>
        </div>
    </ContentField>
</template>

<script>
import ContentField from '../../../components/ContentField.vue'
import { useStore } from 'vuex';
import { ref } from 'vue';
import router from '../../../router/index';
    export default {        
        components: {
            ContentField
        },
        setup() {
            const store = useStore() ;
            let username = ref('') ;
            let password = ref('') ;
            let error_message = ref('') ;
            
            const login = () => {
                error_message.value = "" ;
                store.dispatch("login", {
                    username: username.value,
                    password: password.value,
                    success() {
                        console.log("No") ;
                        store.dispatch("getinfo", {
                            success() {
                                router.push({ name: 'Home'}) ;
                            }
                        })
                    },
                    error() {
                        error_message.value = "用户名或密码错误" ;
                    }
                }) ;

            }

            return {
                username,
                password,
                error_message,
                login,
            }
        }
    }
</script>

<style scoped>
    button {
        width: 100%;
    }
    div.error-message {
        color: red;
    }
</style>