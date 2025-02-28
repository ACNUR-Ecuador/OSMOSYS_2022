const { Builder, By, until } = require('selenium-webdriver');

(async function loginTest() {
  let driver = await new Builder().forBrowser('chrome').build();

  try {
    // Navegar a la página de login
    await driver.get('http://localhost:4200/#/auth/login');

    // Esperar a que el campo de usuario esté presente
    await driver.wait(until.elementLocated(By.id('username')), 5000);
    await driver.wait(until.elementLocated(By.id('password')), 5000);
    await driver.wait(until.elementLocated(By.id('loginButton')), 5000);
    

    // Completar el formulario de login
    
    const usernameField = await driver.findElement(By.id('username'));
    const passwordField = await driver.findElement(By.id('password'));
    const loginButton = await driver.findElement(By.id('loginButton'));

    // Ingresar credenciales
    await usernameField.sendKeys('testuser'); // Reemplaza con el nombre de usuario
    await passwordField.sendKeys('password'); // Reemplaza con la contraseña

    // Hacer clic en el botón de inicio de sesión
    await loginButton.click();

    // Verificar que la redirección fue exitosa (opcional)
    await driver.wait(until.urlContains('/dashboard'), 5000); // Cambia '/dashboard' por la URL esperada después del login
    console.log('Login exitoso, redirigido a:', await driver.getCurrentUrl());
  } catch (error) {
    console.error('Error en la prueba de login:', error);
  } finally {
    await driver.quit();
  }
})();
