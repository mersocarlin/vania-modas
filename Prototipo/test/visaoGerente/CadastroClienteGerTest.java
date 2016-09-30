package visaoGerente;

import junit.framework.TestCase;

/**
 *
 * @author Hemerson e Jefferson
 */
public class CadastroClienteGerTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of validaCPF method, of class CadastroClienteGer.
     */
    public void testValidaCPF() {
        System.out.println("validaCPF");
        String cpf = "051.887.609-83";
        String pessoa = "cliente";
        CadastroClienteGer instance = new CadastroClienteGer();
        instance.validaCPF(cpf, pessoa);
        assertEquals(instance.cpfValido, true);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    public void testValidaCPF2() {
        System.out.println("validaCPF");
        String cpf = "051.887.609-81";
        String pessoa = "cliente";
        CadastroClienteGer instance = new CadastroClienteGer();
        instance.validaCPF(cpf, pessoa);
        assertEquals(instance.cpfValido, false);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of validaNasc method, of class CadastroClienteGer.
     */
    public void testValidaNasc() {
        System.out.println("validaNasc");
        String nascimento = "10/10/1995";
        String pessoa = "cliente";
        CadastroClienteGer instance = new CadastroClienteGer();
        instance.validaNasc(nascimento, pessoa);
        assertEquals(instance.nascValido, false);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    public void testValidaNasc2() {
        System.out.println("validaNasc");
        String nascimento = "10/10/1993";
        String pessoa = "cliente";
        CadastroClienteGer instance = new CadastroClienteGer();
        instance.validaNasc(nascimento, pessoa);
        assertEquals(instance.nascValido, true);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

}
