/*
 * Copyright (c) 2021. Musitique App was developed by Mehmet Oguz Yardimci, Vibhavi Peiris, and Joseph Conwell as CS5704 Software Engineering course assignment.
 *
 * https://www.linkedin.com/in/oguzyardimci/
 * https://www.linkedin.com/in/vibhavipeiris/?originalSubdomain=ca
 * https://conwell.info/
 */

package edu.vt.Pojos;
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
