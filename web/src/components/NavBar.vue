<!-- html -->
<template>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
  <div class="container-fluid">
    <router-link class="navbar-brand" :to = "{name: 'Home'}">King of Bots</router-link>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarText" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarText">
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
        <!-- active控制聚焦 -->
        <li class="nav-item">
          <!-- <a class="nav-link" aria-current="page" href="/pk/">对战</a> -->
          <router-link :class="route_name == 'Pk_Index' ? 'nav-link active' : 'nav-link'" :to = "{name: 'Pk_Index'}">对战</router-link>
        </li>
        <li class="nav-item">
          <!-- <a class="nav-link" href="/record/">对局列表</a> -->
          <router-link :class="route_name == 'Record_Index' ? 'nav-link active' : 'nav-link'" :to = "{name: 'Record_Index'}">对局列表</router-link>

        </li>
        <li class="nav-item">
          <!-- <a class="nav-link" href="/rank_list/">排行榜</a> -->
          <router-link :class="route_name == 'Rank_List' ? 'nav-link active' : 'nav-link'" :to = "{name: 'Rank_List'}">排行榜</router-link>

        </li>
      </ul>
      <span class="navbar-text">
      <ul class="navbar-nav">
        <!-- active控制聚焦 -->
         <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
            oldmomsimith 
          </a>

          <ul class="dropdown-menu dropdown-menu-dark bg-dark">
            <li>
                <!-- <a class="dropdown-item" href="/user/bot/">我的bot</a> -->
                      <router-link class="dropdown-item" :to = "{name: 'User_Bot_Index'}">我的bot</router-link>

            </li>

            <li><a class="dropdown-item" href="#">退出</a></li>
          </ul>
        </li>
 
      </ul>
      </span>
    </div>
  </div>
</nav>
</template>

<!-- 脚本 -->
<script> 
    import { useRoute } from 'vue-router'
    import { computed } from 'vue'

    export default {
        setup() {
            const route = useRoute ;
            let route_name = computed(() => route.name) ;
            return {
                route_name
            }
        }
    }
</script>

<!-- CSS -->
<style scoped>

</style>