package de.skuzzle.cmp.counter.tagcloud;

import static de.skuzzle.cmp.counter.tagcloud.TagCloudAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import de.skuzzle.cmp.counter.client.RestTallyResponse;
import de.skuzzle.cmp.counter.client.Tags;
import de.skuzzle.cmp.counter.client.TestResponses;
import de.skuzzle.cmp.counter.client.TestResponses.TallySheetResponseBuilder;

public class TagCloudTest {

    @Test
    void testGetSelectLink() throws Exception {
        final RestTallyResponse response = prepareResponse(Map.of(
                "tag1", 1));

        final TagCloud tagCloud = TagCloud.fromBackendResponse("anyKey", response, Tags.none());
        final WeightedTag onlyTag = tagCloud.getTags().get(0);
        final String selectLink = onlyTag.getSelectLink(tagCloud);
        assertThat(selectLink).isEqualTo("anyKey?tags=tag1");
    }

    @Test
    void testDeleteLinkNoTagsLeft() throws Exception {
        final RestTallyResponse response = prepareResponse(Map.of(
                "tag1", 1));

        final TagCloud tagCloud = TagCloud.fromBackendResponse("anyKey", response, Tags.fromString("tag1"));
        final WeightedTag onlyTag = tagCloud.getTags().get(0);
        final String selectLink = onlyTag.getDeleteLink(tagCloud);
        assertThat(selectLink).isEqualTo("anyKey");
    }

    @Test
    void testDeleteLink() throws Exception {
        final RestTallyResponse response = prepareResponse(Map.of(
                "tag1", 1,
                "tag2", 1));

        final TagCloud tagCloud = TagCloud.fromBackendResponse("anyKey", response, Tags.fromString("tag1"));
        final WeightedTag onlyTag = tagCloud.getTags().get(1);
        final String selectLink = onlyTag.getDeleteLink(tagCloud);
        assertThat(selectLink).isEqualTo("anyKey?tags=tag1");
    }

    @Test
    void testSingleTagMultipleTimes() throws Exception {
        final RestTallyResponse response = prepareResponse(Map.of(
                "tag", 3));

        final TagCloud tagCloud = TagCloud.fromBackendResponse("anyKey", response, Tags.none());
        assertThat(tagCloud).hasTags("tag");
        assertThat(tagCloud).hasTagWithName("tag")
                .withCount(3)
                .withCssSize("is-size-7");
    }

    @Test
    void testTwoDifferentTagsWithSameCount() throws Exception {
        final RestTallyResponse response = prepareResponse(Map.of(
                "tag1", 1,
                "tag2", 1));

        final TagCloud tagCloud = TagCloud.fromBackendResponse("anyKey", response, Tags.none());
        assertThat(tagCloud).hasTags("tag1", "tag2");
        assertThat(tagCloud)
                .hasTagWithName("tag1")
                .withCssSize("is-size-7")
                .withCount(1);

        assertThat(tagCloud)
                .hasTagWithName("tag2")
                .withCssSize("is-size-7")
                .withCount(1);
    }

    @Test
    void testTwoDifferentCounts() throws Exception {
        final RestTallyResponse response = prepareResponse(Map.of(
                "tag1", 2,
                "tag2", 1));

        final TagCloud tagCloud = TagCloud.fromBackendResponse("anyKey", response, Tags.none());
        assertThat(tagCloud).hasTagWithName("tag1")
                .withCount(2)
                .withCssSize("is-size-6");

        assertThat(tagCloud).hasTagWithName("tag2")
                .withCount(1)
                .withCssSize("is-size-7");
    }

    @Test
    void testThreeDifferentCounts() throws Exception {
        final RestTallyResponse response = prepareResponse(Map.of(
                "tag1", 3,
                "tag2", 2,
                "tag3", 1));

        final TagCloud tagCloud = TagCloud.fromBackendResponse("anyKey", response, Tags.none());
        assertThat(tagCloud).hasTagWithName("tag1")
                .withCount(3)
                .withCssSize("is-size-5");

        assertThat(tagCloud).hasTagWithName("tag2")
                .withCount(2)
                .withCssSize("is-size-6");

        assertThat(tagCloud).hasTagWithName("tag3")
                .withCount(1)
                .withCssSize("is-size-7");
    }

    @Test
    void testFourDifferentCounts() throws Exception {
        final RestTallyResponse response = prepareResponse(Map.of(
                "tag1", 4,
                "tag2", 3,
                "tag3", 2,
                "tag4", 1));

        final TagCloud tagCloud = TagCloud.fromBackendResponse("anyKey", response, Tags.none());
        assertThat(tagCloud).hasTagWithName("tag1")
                .withCount(4)
                .withCssSize("is-size-4");

        assertThat(tagCloud).hasTagWithName("tag2")
                .withCount(3)
                .withCssSize("is-size-5");

        assertThat(tagCloud).hasTagWithName("tag3")
                .withCount(2)
                .withCssSize("is-size-6");

        assertThat(tagCloud).hasTagWithName("tag4")
                .withCount(1)
                .withCssSize("is-size-7");
    }

    @Test
    void testFiveDifferentCounts() throws Exception {
        final RestTallyResponse response = prepareResponse(Map.of(
                "tag1", 5,
                "tag2", 4,
                "tag3", 3,
                "tag4", 2,
                "tag5", 1));

        final TagCloud tagCloud = TagCloud.fromBackendResponse("anyKey", response, Tags.none());
        assertThat(tagCloud).hasTagWithName("tag1")
                .withCount(5)
                .withCssSize("is-size-3");

        assertThat(tagCloud).hasTagWithName("tag2")
                .withCount(4)
                .withCssSize("is-size-4");

        assertThat(tagCloud).hasTagWithName("tag3")
                .withCount(3)
                .withCssSize("is-size-5");

        assertThat(tagCloud).hasTagWithName("tag4")
                .withCount(2)
                .withCssSize("is-size-6");

        assertThat(tagCloud).hasTagWithName("tag5")
                .withCount(1)
                .withCssSize("is-size-7");
    }

    @Test
    void testSixDifferentCounts() throws Exception {
        final RestTallyResponse response = prepareResponse(Map.of(
                "tag1", 5,
                "tag2", 4,
                "tag3", 3,
                "tag4", 2,
                "tag5", 1,
                "tag6", 1));

        final TagCloud tagCloud = TagCloud.fromBackendResponse("anyKey", response, Tags.none());
        assertThat(tagCloud).hasTagWithName("tag1")
                .withCount(5)
                .withCssSize("is-size-3");

        assertThat(tagCloud).hasTagWithName("tag2")
                .withCount(4)
                .withCssSize("is-size-4");

        assertThat(tagCloud).hasTagWithName("tag3")
                .withCount(3)
                .withCssSize("is-size-5");

        assertThat(tagCloud).hasTagWithName("tag4")
                .withCount(2)
                .withCssSize("is-size-6");

        assertThat(tagCloud).hasTagWithName("tag5")
                .withCount(1)
                .withCssSize("is-size-7");
        assertThat(tagCloud).hasTagWithName("tag6")
                .withCount(1)
                .withCssSize("is-size-7");
    }

    private RestTallyResponse prepareResponse(Map<String, Integer> tagToCount) {
        final TallySheetResponseBuilder builder = TestResponses.tallySheet();
        tagToCount.forEach((tag, count) -> {
            for (int i = 0; i < count; ++i) {
                builder.addIncrement(UUID.randomUUID().toString(), "", LocalDateTime.now(), tag);
            }
        });
        return builder.toResponse();
    }
}
