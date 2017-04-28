program example;
var
    x, y: integer;
function gcdRecursive(u, v: integer): integer;
begin
    if
    u mod v <> 0
    then
        gcdRecursive := gcdRecursive(v, u mod v)
    else
        gcdRecursive := v
end;
{function gcdIterative(u, v: integer): integer;
var
    t: integer;
begin
    while
        v <> 0
    do
        begin
            t := u;
            u := v;
            v := t mod v
         end;
    gcdIterative := u
end;}
begin
    read(x);
    read(y);
    write(gcdRecursive(x, y))
end.