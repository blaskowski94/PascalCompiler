package parser.test;

import org.junit.jupiter.api.Test;
import parser.Parser;

/**
 * Created by Bob on 1/30/2017.
 */
class ParserTest {
    @Test
    void program() {
        System.out.println("program");
        Parser instance = new Parser("src/parser/test/simple.pas");
        instance.program();
        System.out.println("It Parsed!");
    }

    @Test
    void identifier_list() {

    }

    @Test
    void declarations() {

    }

    @Test
    void type() {

    }

    @Test
    void standard_type() {

    }

    @Test
    void subprogram_declarations() {

    }

    @Test
    void subprogram_declaration() {

    }

    @Test
    void subprogram_head() {

    }

    @Test
    void arguments() {

    }

    @Test
    void parameter_list() {

    }

    @Test
    void compound_statement() {

    }

    @Test
    void optional_statements() {

    }

    @Test
    void statement_list() {

    }

    @Test
    void statement() {

    }

    @Test
    void variable() {

    }

    @Test
    void procedure_statement() {

    }

    @Test
    void expression_list() {

    }

    @Test
    void expression() {

    }

    @Test
    void simple_expression() {

    }

    @Test
    void simple_part() {

    }

    @Test
    void term() {

    }

    @Test
    void term_part() {

    }

    @Test
    void factor() {

    }

    @Test
    void sign() {

    }

    @Test
    void isRelOp() {

    }

    @Test
    void isAddOp() {

    }

    @Test
    void isMulOp() {

    }

    @Test
    void match() {

    }

    @Test
    void error() {

    }

}