<template>
  <NavBar />
  <router-view/>
</template>

<script>

import NavBar from './components/NavBar.vue'
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap"
// import $ from 'jquery'
export default {
  components: {
    NavBar
  },
  setup() {
    // $.ajax({
    //   url: "http://127.0.0.1:3000/user/account/token/",
    //   type:"post",
    //   data:{
    //     username: "oldmomsimith",
    //     password: "2002"
    //   },
    //   success(resp) {
    //     console.log(resp);
    //   },
    //   error(resp) {
    //     console.log("post" + resp) ;
    //   }
    // })
    // $.ajax({
    //   url: "http://127.0.0.1:3000/user/account/info/",
    //   type: "get",
    //   headers: {
    //     Authorization: "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIwY2FiOGYwODJhYWI0ZjRhODBhMmYzMjk5NDYzMGZkMiIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTcwNjcxMTA1NywiZXhwIjoxNzA3OTIwNjU3fQ.wkfKPPJlCrKB7_gL2RjS71HSf6aGk9axrpFjHzgU4BI"
    //   },
    //   success(resp) {
    //     console.log(resp) ;
    //   },
    //   error(resp) {
    //     console.log(resp) ;
    //   }
    // })
    // $.ajax({
    //   url: "http://localhost:3000/user/account/register/",
    //   type: "post",
    //   data: {
    //     username: "test1",
    //     password: "2002",
    //     confirmedpassword: "2002"
    //   },
    //   success(resp) {
    //     console.log(resp) ;
    //   },
    //   error(resp) {
    //     console.log(resp) ;
    //   }
    // })
  }
}

</script>
<style>
body {
  background-image: url("@/assets/image/background.jpg");
  background-size: cover;
}
</style>
