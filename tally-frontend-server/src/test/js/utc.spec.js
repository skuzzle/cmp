import { registerPrototype } from 'utc.js'

test("Prototype should be registered", () => {
	registerPrototype();
	expect(Date.prototype.toInputString).toBeDefined();
})

test("Get Date as UTC", () => {
	registerPrototype();
	const sample = new Date(Date.UTC(2019, 10, 7, 17, 42, 0));
	const formatted = sample.toInputString();
	expect(formatted).toBe("2019-11-07");
})
