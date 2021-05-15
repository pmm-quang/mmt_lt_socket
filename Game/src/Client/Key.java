package Client;

enum Key{
    CHAT, PLAY, GAME, INVITE, LOGIN, QUIT, ACCEPT, EXIT, WIN, HIGHSOCRE;

    public String toString() {
        switch (this) {
            case CHAT: return "CHAT";
            case PLAY: return "PLAY";
            case GAME: return "GAME";
            case INVITE: return "INVITE";
            case LOGIN: return "LOGIN";
            case QUIT: return  "QUIT";
            case ACCEPT: return "ACCEPT";
            case EXIT: return "EXIT";
            case WIN: return "WIN";
            case HIGHSOCRE: return "HIGHSOCRE";
            default: return null;
        }
    }

}

