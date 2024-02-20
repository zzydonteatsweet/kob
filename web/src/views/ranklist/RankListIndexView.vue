<template>
    <ContentField>
            <table class="table table-dark table-striped">
                <thead>
                    <tr>
                        <th>玩家</th>
                        <th>天梯分</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="user in users" :key="user.id">
                        <td>
                            <img :src="user.photo" alt="" class="user-photo">
                            &nbsp;
                            <span class="user-username">{{ user.username }}</span>
                        </td>
                        <td>{{ user.rating }}</td>

                    </tr>
                </tbody>
            </table>

            <nav aria-label="Page navigation example" style="float: right;">
                <ul class="pagination">
                    <li class="page-item" @click="click_page(-2)">
                        <a class="page-link" href="#">Previous</a>
                    </li>
                    <li :class="'page-item ' + page.is_active" v-for="page in pages" :key="page.number" @click="click_page(page.number)">
                        <a class="page-link" href="#">{{ page.number }}</a>
                    </li>
                    <li class="page-item" @click="click_page(-1)">
                        <a class="page-link" href="#">Next</a>
                    </li>
                </ul>
            </nav>

    </ContentField>
</template>

<script>
import ContentField from '../../components/ContentField.vue'
import { useStore } from 'vuex';
import $ from 'jquery';
import { ref } from 'vue';
    export default {
        components: {
            ContentField
        },
        setup() {
            const store = useStore();
            let current_page = 1;
            let users = ref([]);
            let total_users = 0;
            let pages = ref([]);

            const update_page = () => {
                let max_page = parseInt(Math.ceil(total_users /3));
                let new_page = [];
                for(let i = current_page - 2 ; i <= current_page + 2 ; i ++) {
                    if (i >= 1 && i <= max_page) {
                        new_page.push({
                            number: i,
                            is_active: i == current_page ? "active" : "",
                        });
                    }
                }
                pages.value = new_page;
            };

            const click_page = page => {
                if (page === -2) page = current_page - 1;
                else if(page === -1) page = current_page + 1;
                let max_page = parseInt(Math.ceil(total_users / 3));

                if(page >= 1 && page <= max_page) {
                    pull_page(page);
                }

            };
            const pull_page = page => {
                current_page = page;
                $.ajax({
                    url: "http://127.0.0.1:3000/ranklist/getlist/",
                    type: "get",
                    data: {
                        page,
                    },
                    headers: {
                        Authorization: "Bearer " + store.state.user.token
                    },
                    success(resp) {
                        users.value = resp.users;
                        total_users = resp.users_count;
                        update_page();
                    },
                    error(resp) {
                        console.log(resp);
                    }
                })
            };
            pull_page(current_page);
            

           return {
                users, 
                pages,
                click_page,
            }
        }
    }
</script>

<style scoped>
img.user-photo {
    width: 4vw;
    border-radius: 50%;
}
</style>