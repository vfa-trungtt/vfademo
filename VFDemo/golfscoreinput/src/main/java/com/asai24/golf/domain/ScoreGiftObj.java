package com.asai24.golf.domain;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class ScoreGiftObj implements Serializable, Cloneable {
    private String club_name;
    private String club_address;
    private String course_name;
    private String weather;
    private long play_date;
    private ArrayList<PlayerObj> players;
    private ArrayList<HolesObj> holes;
    private ScoreObj out;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }

    public ScoreObj getOut() {
        return out;
    }


    public void setOut(ScoreObj out) {
        this.out = out;
    }


    public ScoreObj getIn() {
        return in;
    }


    public void setIn(ScoreObj in) {
        this.in = in;
    }


    public ScoreObj getTotal() {
        return total;
    }


    public void setTotal(ScoreObj total) {
        this.total = total;
    }


    private ScoreObj in;
    private ScoreObj total;

    public String getClub_name() {
        return club_name;
    }


    public void setClub_name(String club_name) {
        this.club_name = club_name;
    }


    public String getClub_address() {
        return club_address;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public void setClub_address(String club_address) {
        this.club_address = club_address;
    }


    public String getWeather() {
        return weather;
    }


    public void setWeather(String weather) {
        this.weather = weather;
    }


    public long getPlay_date() {
        return play_date;
    }


    public void setPlay_date(long play_date) {
        this.play_date = play_date;
    }


    public ArrayList<PlayerObj> getPlayers() {
        return players;
    }


    public void setPlayers(ArrayList<PlayerObj> players) {
        this.players = players;
    }


    public ArrayList<HolesObj> getHoles() {
        return holes;
    }


    public void setHoles(ArrayList<HolesObj> holes) {
        this.holes = holes;
    }

    public class PlayerObj implements Serializable {
        private String id;
        private boolean owner_flag;

        public boolean isOwner_flag() {
            return owner_flag;
        }

        public void setOwner_flag(boolean owner_flag) {
            this.owner_flag = owner_flag;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPlayerHdcp() {
            return playerHdcp;
        }

        public void setPlayerHdcp(String playerHdcp) {
            this.playerHdcp = playerHdcp;
        }

        private String name;
        private String playerHdcp;
    }

    public class HolesObj implements Serializable {
        private int par;
        private int hole_number;
        private int distance;
        private ArrayList<HolePlayerObj> hole_player;

        public int getPar() {
            return par;
        }

        public void setPar(int par) {
            this.par = par;
        }

        public int getHole_number() {
            return hole_number;
        }

        public void setHole_number(int hole_number) {
            this.hole_number = hole_number;
        }

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public ArrayList<HolePlayerObj> getHole_player() {
            return hole_player;
        }

        public void setHole_player(ArrayList<HolePlayerObj> hole_player) {
            this.hole_player = hole_player;
        }

        public class HolePlayerObj implements Serializable {
            private int putt;
            private String id;
            private int hole_score;
            private String game_point;
            private boolean puttDisabled;

            public int getPutt() {
                return putt;
            }

            public void setPutt(int putt) {
                this.putt = putt;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public int getHole_score() {
                return hole_score;
            }

            public void setHole_score(int hole_score) {
                this.hole_score = hole_score;
            }

            public String getGamePoint() {
                return this.game_point;
            }

            public void setGamePoint(String game_point) {
                this.game_point = game_point;
            }

            public boolean isPuttDisabled() {
                return puttDisabled;
            }

            public void setPuttDisabled(boolean puttDisabled) {
                this.puttDisabled = puttDisabled;
            }

            public String getPuttText() {
                return puttDisabled ? "" : putt + "";
            }
        }
    }


    public class ScoreObj implements Serializable {
        private int distance;

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public int getPar() {
            return par;
        }

        public void setPar(int par) {
            this.par = par;
        }

        public int getPutt() {
            return putt;
        }

        public void setPutt(int putt) {
            this.putt = putt;
        }

        public ArrayList<PlayerScore> getPlayers() {
            return players;
        }

        public void setPlayers(ArrayList<PlayerScore> players) {
            this.players = players;
        }

        private int par;
        private int putt;
        private ArrayList<PlayerScore> players;

        public class PlayerScore implements Serializable {
            private String id;
            private int putt;
            private int score;
            private String game_point;
            private boolean puttDisabled;

            public int getPutt() {
                return putt;
            }

            public void setPutt(int putt) {
                this.putt = putt;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public int getScore() {
                return score;
            }

            public void setScore(int score) {
                this.score = score;
            }

            public String getGamePoint() {
                return this.game_point;
            }

            public void setGamePoint(String game_point) {
                this.game_point = game_point;
            }

            public boolean isPuttDisabled() {
                return puttDisabled;
            }

            public void setPuttDisabled(boolean puttDisabled) {
                this.puttDisabled = puttDisabled;
            }

            public String getPuttText() {
                return puttDisabled ? "" : putt + "";
            }
        }
    }


}
