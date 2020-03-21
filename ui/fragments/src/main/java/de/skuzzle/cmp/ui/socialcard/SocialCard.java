package de.skuzzle.cmp.ui.socialcard;

import java.util.Objects;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

public class SocialCard {

    // static for now
    private final String card = "summary";
    private final String site = "@CountMyPizza";
    private final String title;
    private final String description;
    private final String image;
    private final String imageAlt;

    private SocialCard(String title, String description, String image, String imageAlt) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.imageAlt = imageAlt;
    }

    public static Builder withTitle(String title) {
        return new Builder().withTitle(title);
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getSite() {
        return this.site;
    }

    public String getImage() {
        return this.image;
    }

    public String getImageAlt() {
        return this.imageAlt;
    }

    public String getCard() {
        return this.card;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, image, imageAlt, card, site);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof SocialCard
                && Objects.equals(title, ((SocialCard) obj).title)
                && Objects.equals(description, ((SocialCard) obj).description)
                && Objects.equals(image, ((SocialCard) obj).image)
                && Objects.equals(imageAlt, ((SocialCard) obj).imageAlt)
                && Objects.equals(card, ((SocialCard) obj).card)
                && Objects.equals(site, ((SocialCard) obj).site);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("title", title)
                .add("description", description)
                .add("image", image)
                .add("imageAlt", imageAlt)
                .toString();
    }

    public static class Builder {
        private String title;
        private String description;
        private String image;
        private String imageAlt;

        private Builder withTitle(String title) {
            Preconditions.checkArgument(title != null && !title.isEmpty(), "title must not be null/empty");
            this.title = title;
            return this;
        }

        public Builder withDescription(String description) {
            Preconditions.checkArgument(description != null && !description.isEmpty(), "description must not be empty");
            this.description = description;
            return this;
        }

        public Builder withImage(String imageUrl) {
            Preconditions.checkArgument(imageUrl != null && !imageUrl.isEmpty(), "imageUrl must not be null/empty");
            this.image = imageUrl;
            return this;
        }

        public Builder withImageAlt(String imageAlt) {
            Preconditions.checkArgument(imageAlt != null && !imageAlt.isEmpty(), "imageAlt must not be null/empty");
            this.imageAlt = imageAlt;
            return this;
        }

        public SocialCard build() {
            return new SocialCard(title, description, image, imageAlt);
        }
    }
}
