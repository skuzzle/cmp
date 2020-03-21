package contracts.tally

import org.springframework.cloud.contract.spec.internal.RegexProperty

import java.util.regex.Pattern

class Helpers {
    protected static final Pattern ISO_DATE_TIME_WITH_NANOS = Pattern.
            compile('([0-9]{4})-(1[0-2]|0[1-9])-(3[01]|0[1-9]|[12][0-9])T(2[0-3]|[01][0-9]):([0-5][0-9]):([0-5][0-9])\\.\\d+')

    static RegexProperty isoDateTimeWithNanos() {
        return new RegexProperty(ISO_DATE_TIME_WITH_NANOS).asString()
    }
}

org.springframework.cloud.contract.spec.Contract.make {
    name("createTallySheet")
    request {
        method POST()
        url $(consumer(~/\/[a-zA-Z0-9]+/), producer('/name'))
        headers {}
    }
    response {
        status CREATED()
        body([
            tallySheet: [
                name: fromRequest().path(0),
                publicKey: 'publicKey4',
                adminKey: 'adminKey4',
                createDateUTC: $(
                    consumer('1987-09-12T11:11:00.123'),
                    producer(regex(Helpers.isoDateTimeWithNanos()))
                ),
                lastModifiedDateUTC: $(
                    consumer('1987-09-12T11:11:00.123'),
                    producer(regex(Helpers.isoDateTimeWithNanos()))
                ),
                totalCount: 0,
                assignableToCurrentUser: false
            ],
            increments: [
                total: 0,
                start: 0,
                entries: []
            ],
            shareDefinitions: []
        ])
        headers {
            contentType(applicationJson())
        }
    }
}
