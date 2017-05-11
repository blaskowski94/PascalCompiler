program arrays;
var
    x : integer;
var
    ary : array[0:10] of integer;
function loop(a : array [0:10] of integer): integer;
var
    count: integer;
begin
    count := 0;
    while
    count < 10
    do
    begin
        write(a[count]);
        count := count + 1
    end;
    loop := count
end;
begin
    ary[0] := 1;
    write(ary[0]);
    ary[1] := 2;
    write(ary[1]);
    ary[2] := 3;
    write(ary[2]);
    ary[3] := 4;
    write(ary[3]);
    ary[4] := 5;
    write(ary[4]);
    ary[5] := 6;
    write(ary[5]);
    ary[6] := 7;
    write(ary[6]);
    ary[7] := 8;
    write(ary[7]);
    ary[8] := 9;
    write(ary[8]);
    ary[9] := 10;
    write(ary[9]);
    write(loop(ary))
end.