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
    name("incrementExistingTallySheet")
    request {
        method POST()
        url '/adminKey1/increment'
        body([
                id: null,
                incrementDateUTC: $(
                        consumer(regex(Helpers.isoDateTimeWithNanos())),
                        producer("2019-04-12T11:21:32.123")),
                description: "Description",
                tags: [ 'tag1', 'tag2']
        ])
        headers {
            contentType(applicationJson())
        }
    }
    response {
        status OK()
    }
}
