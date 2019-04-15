package contracts

org.springframework.cloud.contract.spec.Contract.make {
    name("incrementExistingTallySheet")
    request {
        method POST()
        url '/admin/adminKey'
        body([
            description: "Description",
            tags: [ 'tag1', 'tag2']
        ])
        headers {
            contentType(applicationJson())
        }
    }
    response {
        status OK()
        body([
            id: $(
                consumer('5c9dc2ce8691ff4f8c1b2d54'),
                producer(regex('[a-zA-Z0-9]+'))
            ),
            name: 'existing',
            publicKey: 'publicKey',
            adminKey: 'adminKey',
            createDateUTC: $(
                consumer('1987-09-12T11:11:00'),
                producer(regex(isoDateTime()))
            ),
            lastModifiedDateUTC: $(
                consumer('1987-09-12T11:11:00'),
                producer(regex(isoDateTime()))
            ),
            increments: [
                [
                    description: regex('\\w+'),
                    tags: [ 'tag1', 'tag2' ],
                    createDateUTC: $(
                        consumer('1987-09-12T11:11:00'),
                        producer(regex(isoDateTime()))
                    )
                ]
            ]
        ])
        headers {
            contentType(applicationJson())
        }
    }
}
