package contracts.tally

import org.springframework.cloud.contract.spec.internal.RegexProperty

import java.util.regex.Pattern

class Helpers {
    protected static final String ISO_DATE_TIME_WITH_NANOS = '([0-9]{4})-(1[0-2]|0[1-9])-(3[01]|0[1-9]|[12][0-9])T(2[0-3]|[01][0-9]):([0-5][0-9]):([0-5][0-9])\\.\\d+'

}

org.springframework.cloud.contract.spec.Contract.make {
    name("getExistingTallySheetsForUser")
    request {
        method GET()
        url '/'
        headers {}
    }
    response {
        status OK()
        body([
            tallySheets: [
                [
                    name: 'existing1',
                    shareDefinitions: [
                        [
                            shareId: 'shareId1',
                            shareInformation: [
                                showIncrements: true,
                                showIncrementTags: true,
                                showIncrementDescription: true
                            ]
                        ]
                    ],
                    adminKey: 'adminKey1',
                    createDateUTC: $(
                        consumer('1987-09-12T11:11:00.123'),
                        producer(matching(Helpers.ISO_DATE_TIME_WITH_NANOS))
                    ),
                    lastModifiedDateUTC: $(
                        consumer('1987-09-12T11:11:00.123'),
                        producer(regex(Helpers.ISO_DATE_TIME_WITH_NANOS))
                    )
                ]
            ]
        ])
        headers {
            contentType(applicationJson())
        }
    }
}
