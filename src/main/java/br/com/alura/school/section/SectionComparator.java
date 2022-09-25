package br.com.alura.school.section;

import java.util.Comparator;

public class SectionComparator implements Comparator<Section> {

    @Override
    public int compare(Section o1, Section o2) {
        return  o2.getVideos().size() - o1.getVideos().size();
    }
}
