<template>
    <div class="container">
        <div class="row">
            <div class="col-3">
                <div class="card" style="margin-top: 20px;">
                    <div class="card-body">
                        <img :src="$store.state.user.photo" >
                    </div>
                </div>
            </div>
            <div class="col-9">
                <div class="card" style="margin-top: 20px;">
                    <div class="card-header">
                       <span style="font-size: 120%;">我的bot</span> 
                        <button type="button" class="btn btn-primary float-end" data-bs-toggle="modal" data-bs-target="#add-bot-btn">
                            创建bot
                        </button>
                    </div>

                        <!-- Modal -->
                        <div class="modal fade" id="add-bot-btn" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                            <div class="modal-dialog modal-xl">
                                <div class="modal-content">
                                <div class="modal-header">
                                    <h1 class="modal-title fs-5">创建bot</h1>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>
                                <div class="modal-body">
                                    <div class="mb-3">
                                        <label for="add-bot-title" class="form-label">名称</label>
                                        <input v-model="bot_add.name" type="text" class="form-control" id="add-bot-title" placeholder="请输入bot名称">
                                    </div>
                                    <div class="mb-3">
                                        <label for="add-bot-description" class="form-label">bot简介</label>
                                        <textarea v-model="bot_add.description" class="form-control" id="add-bot-description" rows="5" placeholder="bot简介"></textarea>
                                    </div>
                                    <div class="mb-3">
                                        <label for="add-bot-code" class="form-label">bot代码</label>
                                        <VAceEditor
                                            v-model:value="bot_add.content"
                                            @init="editorInit"
                                            lang="c_cpp"
                                            theme="textmate"
                                            style="height: 300px" />
                                    </div>


                                </div>
                                    <div class="modal-footer">
                                        <div class="error-message" style="color: red;">{{ bot_add.error_message }}</div>
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">关闭</button>
                                        <button type="button" class="btn btn-primary" @click="add_bot">提交</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    <div class="card-body">
                        <table class="table table-dark table-striped">
                            <thead>
                                <tr>
                                    <th>名称</th>
                                    <th>创建时间</th>
                                    <th>操作</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="bot in bots" :key="bot.id">
                                    <td>{{ bot.name }}</td>
                                    <td>{{ bot.createTime }}</td>
                                    <td>
                                        <button type="button" class="btn btn-success" style="margin-right: 10px;"  data-bs-toggle="modal" :data-bs-target="'#update-bot-modal-' + bot.id">修改</button>
                                        <button type="button" class="btn btn-danger" @click="remove_bot(bot.id)">删除</button>

                                        <div class="modal fade" :id="'update-bot-modal-' + bot.id" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                                            <div class="modal-dialog modal-xl">
                                                <div class="modal-content">
                                                <div class="modal-header">
                                                    <h1 class="modal-title fs-5">修改bot</h1>
                                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                </div>
                                                <div class="modal-body">
                                                    <div class="mb-3">
                                                        <label for="update-bot-name" class="form-label">名称</label>
                                                        <input v-model="bot.name" type="text" class="form-control" id="update-bot-name" placeholder="请输入bot名称">
                                                    </div>
                                                    <div class="mb-3">
                                                        <label for="update-bot-description" class="form-label">bot简介</label>
                                                        <textarea v-model="bot.description" class="form-control" id="update-bot-description" rows="5" placeholder="bot简介"></textarea>
                                                    </div>
                                                    <div class="mb-3">
                                                        <label for="update-bot-code" class="form-label">bot代码</label>
                                                        <VAceEditor
                                                            v-model:value="bot.content"
                                                            @init="editorInit"
                                                            lang="c_cpp"
                                                            theme="textmate"
                                                            style="height: 300px" />
                                                    </div>


                                                </div>
                                                    <div class="modal-footer">
                                                        <div class="error-message" style="color: red;">{{ bot.error_message }}</div>
                                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">关闭</button>
                                                        <button type="button" class="btn btn-primary" @click="update_bot(bot)">保存修改</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div> 
</template>

<script>
// import ContentField from '../../../components/ContentField.vue'
import $ from 'jquery'
import { ref, reactive } from 'vue';
import { useStore } from 'vuex';
import { Modal } from 'bootstrap/dist/js/bootstrap';
import { VAceEditor } from 'vue3-ace-editor';
import ace from 'ace-builds';
export default {
    components: {
        // ContentField
        VAceEditor,
        
    },
    setup() {
        ace.config.set(
            "basePath", 
            "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/")



        const store = useStore() ;
        let bots = ref([]) ;
        const bot_add = reactive({
            name: "",
            description: "",
            content: "",
            error_message: "",
        }) 
        const refresh_bots = () => {
            $.ajax({
                url: "http://127.0.0.1:3000/user/bot/getlist/",
                type: "get",
                headers: {
                    Authorization: "Bearer " + store.state.user.token
                },
                success(resp) {
                    bots.value = resp ;
                    // console.log(resp) ;
                }
            })
        }
        refresh_bots() ;

        const add_bot = () => {
            bot_add.error_message = "" ;
            $.ajax({
                url:"http://127.0.0.1:3000/user/bot/add/",
                type: "post",
                data: {
                    name: bot_add.name,
                    description: bot_add.description,
                    content: bot_add.content,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    if(resp.error_message === "success") {
                        bot_add.name = "" ;
                        bot_add.content = "" ;
                        bot_add.desciption = "" ;
                        Modal.getInstance("#add-bot-btn").hide() ;
                        refresh_bots() ;
                    }else {
                        bot_add.error_message = resp.error_message ;
                    }
                }

            })
        }

        const remove_bot = (bot_id) => {
            $.ajax({
                url:"http://127.0.0.1:3000/user/bot/remove/",
                type: "post",
                data: {
                    bot_id: bot_id,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success() {
                    refresh_bots() ;
                }
            })
        }

        const update_bot = (bot) => {
            bot.error_message = "" ;
            $.ajax({
                url:"http://127.0.0.1:3000/user/bot/update/",
                type: "post",
                data: {
                    bot_id: bot.id,
                    name: bot.name,
                    description: bot.description,
                    content: bot.content,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    console.log(resp) ;
                    if(resp.error_message === "success") {
                        Modal.getInstance('#update-bot-modal-' + bot.id).hide() ;
                        refresh_bots() ;
                    }else {
                        bot.error_message = resp.error_message ;
                    }
                }

            })
        }

        return {
            bots,
            bot_add,
            add_bot,
            remove_bot,
            update_bot,
        }
    }
}
</script>

<style scoped>

</style>