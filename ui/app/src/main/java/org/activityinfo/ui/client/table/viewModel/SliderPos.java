package org.activityinfo.ui.client.table.viewModel;

import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.ui.client.table.TablePlace;

import java.util.List;
import java.util.Objects;

public class SliderPos {

    private int slideIndex;
    private final List<ResourceId> path;

    public SliderPos(SliderTree sliderTree, TablePlace place) {
        this.slideIndex = sliderTree.getSlideIndex(place.getFormId());
        this.path = sliderTree.findPathToRoot(place.getFormId());
    }

    public int getSlideIndex() {
        return slideIndex;
    }

    public boolean isVisible(ResourceId formId) {
        return path.contains(formId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SliderPos sliderPos = (SliderPos) o;
        return slideIndex == sliderPos.slideIndex &&
                Objects.equals(path, sliderPos.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(slideIndex, path);
    }
}
