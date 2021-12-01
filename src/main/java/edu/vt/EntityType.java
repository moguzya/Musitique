package edu.vt;
public enum EntityType {
    TRACK{
        public String toString() {
            return "TRACK";
        }
    },
    ALBUM{
        public String toString() {
            return "ALBUM";
        }
    },
    ARTIST{
        public String toString() {
            return "ARTIST";
        }
    }
}
