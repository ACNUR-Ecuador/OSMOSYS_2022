const { Builder, By, until } = require('selenium-webdriver');

(async function test() {
  let driver = await new Builder().forBrowser('chrome').build();

  try {
    await driver.get('http://localhost:4200');
    await driver.wait(until.titleIs('Osmosys'), 5000); // Cambia 'expected-title' por el título esperado
    console.log(await driver.getTitle());
  } catch (error) {
    console.error('Error:', error);
  } finally {
    await driver.quit();
  }
})();