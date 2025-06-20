package sideproject.gugumo.page;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

@Getter
public class PageableCustom {

    private int number;
    private int size;
    private Sort sort;
    private boolean first;
    private boolean last;
    private boolean hasNext;
    private int totalPages;
    private long totalElements;
    private long numberOfElements;
    private boolean isEmpty;


    public PageableCustom(Page page) {
        this.number = page.getNumber() + 1;
        this.size = page.getSize();
        this.sort = page.getSort();
        this.first = page.isFirst();
        this.last = page.isLast();
        this.hasNext = page.hasNext();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.numberOfElements = page.getNumberOfElements();
        this.isEmpty = page.isEmpty();


    }

    public PageableCustom(Slice slice) {
        this.first = slice.isFirst();
        this.last = slice.isLast();
        this.hasNext = slice.hasNext();
        this.number = slice.getNumber() + 1;
        this.size = slice.getSize();
    }


}
