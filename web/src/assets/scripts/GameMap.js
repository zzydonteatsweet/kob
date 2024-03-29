import { AcGameObject } from "./AcGameObjects";
import { Wall } from "./Wall";
import { Snake } from "./Snake";

export class GameMap extends AcGameObject {
    constructor(ctx, parent, store) {
        super();

        this.ctx = ctx;
        this.parent = parent;
        this.store = store ;

        this.L = 0;

        this.rows = 13;
        this.cols = 14;
        
        this.inner_walls_count = 20;
        this.walls = [];

        this.snakes = [
            new Snake({id: 0, color: "#4876ec", r: this.rows - 2, c: 1}, this),
            new Snake({id:1, color: "#f94848", r: 1, c: this.cols-2}, this),
        ]
    }

    next_step() {
        for(const snake of this.snakes) {
            snake.next_step() ;
        }
    }

    check_ready() {
        for (const snake of this.snakes) {
            if(snake.status !== "idle") return false ;
            if(snake.direction === -1) return false ;
        }
        return true; 
    }

    create_walls() {
        const g = this.store.state.pk.gamemap ;
        for (let r = 0; r < this.rows; r ++ ) {
            for (let c = 0; c < this.cols; c ++ ) {
                if (g[r][c]) {
                    this.walls.push(new Wall(r, c, this));
                }
            }
        }

        return true;
    }

    add_listening_events() {
        if (this.store.state.record.is_record) {
            let k = 0;
            const a_steps = this.store.state.record.a_steps;
            const b_steps = this.store.state.record.b_steps;           
            const [snake0, snake1] = this.snakes;
            const loser = this.store.state.record.record_loser;
            const interval_id = setInterval(() => {
                if (k >= a_steps.length - 1) {
                    if(loser === "all" || loser === "a") {
                        snake0.status = "dead" ;
                    }
                    if(loser === "all" || loser === "b") {
                        snake1.status = "dead" ;
                    }
                    this.store.commit("updateLoser", loser) ;

                    clearInterval(interval_id);
                }else {
                    snake0.set_direction(parseInt(a_steps[k]));
                    snake1.set_direction(parseInt(b_steps[k]));
                    k ++;
                }
            }, 300);
        }else {
            this.ctx.canvas.focus() ;

            // const [snake0, snake1] = this.snakes ;
            this.ctx.canvas.addEventListener("keydown", e => {
                let d = -1 ;
                if (e.key === 'w') d = 0 ;
                else if (e.key === 'd') d = 1 ;
                else if (e.key === 's') d = 2 ;
                else if (e.key === 'a') d = 3 ;
                // else if (e.key === 'ArrowUp') this.snakes[1].set_direction(0);
                // else if (e.key === 'ArrowRight') this.snakes[1].set_direction(1);
                // else if (e.key === 'ArrowDown') this.snakes[1].set_direction(2);
                // else if (e.key === 'ArrowLeft') this.snakes[1].set_direction(3);

                if(d >= 0) {
                    console.log("move push down" + d);
                    this.store.state.pk.socket.send(JSON.stringify({
                        event: "move",
                        direction: d,
                    })) ;
                }
            }) ;

        }
    }

    start() {
        this.create_walls() ;
        this.add_listening_events() ;
    }


    update_size() {
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;
    }

    update() {
        this.update_size();
        if(this.check_ready()) {
            this.next_step() ;
        }
        this.render();
    }

    render() {
        const color_even = "#AAD751", color_odd = "#A2D149";
        for (let r = 0; r < this.rows; r ++ ) {
            for (let c = 0; c < this.cols; c ++ ) {
                if ((r + c) % 2 == 0) {
                    this.ctx.fillStyle = color_even;
                } else {
                    this.ctx.fillStyle = color_odd;
                }
                this.ctx.fillRect(c * this.L, r * this.L, this.L, this.L);
            }
        }
    }
}
