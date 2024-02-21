<template>
    <ContentField>
            <table class="table table-dark table-striped">
                <thead>
                    <tr>
                        <th>A</th>
                        <th>B</th>
                        <th>对战结果</th>
                        <th>对战时间</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="record in records" :key="record.record.id">
                        <td>
                            <img :src="record.a_photo" alt="" class="record-user-photo">
                            &nbsp;
                            <span class="record-user-username">{{ record.a_username }}</span>
                        </td>
                        <td>
                            <img :src="record.b_photo" alt="" class="record-user-photo">
                            &nbsp;
                            <span class="record-user-username">{{ record.b_username }}</span>
                        </td>
                        <td>
                            <span class="record-result">{{ record.result }}</span>
                        </td>
                        <td>
                            {{ record.record.createtime }}
                        </td>
                        <td>
                            <button @click="open_record_content(record.record.id)" type="button" class="btn btn-secondary">查看录像</button>
                        </td>
                    </tr>
                </tbody>
            </table>

            <nav aria-label="Page navigation example" style="float: right;">
                <ul class="pagination">
                    <li class="page-item" @click="click_page(-2)">
                        <a class="page-link" href="#">Previous</a>
                    </li>
                    <li :class="'page-item ' + page.is_active" v-for="page in page" :key="page.number" @click="click_page(page.number)">
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
import router from '@/router';
    export default {
        components: {
            ContentField
        },
        setup() {
            const store = useStore();
            let current_page = 1;
            let records = ref([]);
            let total_records = 0;
            let page = ref([]);

            const update_page = () => {
                let max_page = parseInt(Math.ceil(total_records /10));
                let new_page = [];
                for(let i = current_page - 2 ; i <= current_page + 2 ; i ++) {
                    if (i >= 1 && i <= max_page) {
                        new_page.push({
                            number: i,
                            is_active: i == current_page ? "active" : "",
                        });
                    }
                }
                page.value = new_page;
            };
            console.log(total_records);

            const click_page = page => {
                if (page === -2) page = current_page - 1;
                else if(page === -1) page = current_page + 1;
                let max_page = parseInt(Math.ceil(total_records /10));

                if(page >= 1 && page <= max_page) {
                    pull_page(page);
                }

            };
            const pull_page = page => {
                current_page = page;
                $.ajax({
                    url: "https://app3403.acapp.acwing.com.cn/api/record/getlist/",
                    type: "get",
                    data: {
                        page,
                    },
                    headers: {
                        Authorization: "Bearer " + store.state.user.token
                    },
                    success(resp) {
                        records.value = resp.records;
                        total_records = resp.record_count;
                        update_page();
                    },
                    error(resp) {
                        console.log(resp);
                    }
                })
            };
            pull_page(current_page);
            
            const stringTo2D = map => {
                let g = [];
                for(let i = 0, k = 0 ; i < 13 ; i ++) {
                    let line = [];
                    for(let j = 0 ; j < 14 ; j ++, k ++) {
                        if(map[k] == '0') line.push(0);
                        else line.push(1);
                    }
                    g.push(line);
                }
                return g;
            }

            const open_record_content = recordId => {
                console.log(records.value);
                for (const record of records.value) {
                    if (record.record.id === recordId) {
                        store.commit("updateIsRecord", true);
                        store.commit("updateGame", {
                            map: stringTo2D(record.record.map),
                            a_id: record.record.a_id,
                            a_sx: record.record.asx,
                            a_sy: record.record.asy,
                            b_id: record.record.bid,
                            b_sx: record.record.bsx,
                            b_sy: record.record.bsy,
                           

                        });
                        store.commit("updateSteps", {
                            a_steps: record.record.asteps,
                            b_steps: record.record.bsteps,
                        });
                        store.commit("updateRecordLoser", record.record.loser);
                        router.push({
                            name: "Record_content",
                            params: {
                                recordId: recordId, //  也可简写一个
                            }
                        })            
                        break;
                    }
                }
            }
            return {
                records, 
                open_record_content,
                page,
                click_page,
            }
        }
    }
</script>

<style scoped>
img.record-user-photo {
    width: 4vw;
    border-radius: 50%;
}
</style>