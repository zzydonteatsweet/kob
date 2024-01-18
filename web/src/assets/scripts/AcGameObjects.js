const Ac_Game_Objects = [] ;

export class AcGameObject {
    constructor() {
        Ac_Game_Objects.push(this) ;
        this.time_delta = 0 ;
        this.has_calledd_start = false ;
    }

    start() {

    }

    update() {

    }

    on_destroy() {

    }

    destroy() {
        this.on_destroy() ;

        for (let i in Ac_Game_Objects) {
            const obj = Ac_Game_Objects[i] ;
            if(obj === this) {
                Ac_Game_Objects.splice(i) ; 
                break ;
            }
        }
    }
}

let last_timestap ;
const step = timestap => {
    for (let obj of Ac_Game_Objects) {
        if(!obj.has_calledd_start) { 
            obj.has_calledd_start = true ;
            obj.start() ;
        }else {
            obj.time_delta = timestap - last_timestap ;
            obj.update() ;
        }
    }
    last_timestap = timestap ;
    requestAnimationFrame(step) ;
}

requestAnimationFrame(step) ;