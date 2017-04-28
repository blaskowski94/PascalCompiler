{ sample.pas }
{ An example program for a syntax tree.}
{ Exchange rates as of 9 March 2017.}

program sample;
var
    dollars, yen, bitcoins: integer;

begin
    dollars := 1000000;
    yen := dollars * 114;
    write(yen);
    bitcoins := dollars / 1184;
    write(bitcoins)
end
.
