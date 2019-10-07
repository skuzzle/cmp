import { registerPrototype, utcToLocal } from 'utc.js'

test("Prototype should be registered", () => {
	registerPrototype();
	expect(Date.prototype.toInputString).toBeDefined();
})

test("Get Date as UTC", () => {
	registerPrototype();
	const sampleUTC = new Date(Date.UTC(2019, 10, 7, 17, 42, 0));
	const formatted = sampleUTC.toInputString();
	expect(formatted).toBe("2019-11-07");
})

test("convert utc to local simple", () => {
	const formatted = utcToLocal("2019-09-07T18:16:32.489");
	expect(formatted).toBe("2019-09-07");
})
