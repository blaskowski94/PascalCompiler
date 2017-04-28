{ This is the simplest pascal program }

program Recursive;
var
    fo: integer;
var
    arr: array[3:5] of integer;
function times(numOne, numTwo: integer): integer;
var
    result, num: integer;
begin
    if numTwo = 1
    then
        times := numOne
    else
        times := numOne + times(numOne, numTwo - 1)
end;
begin
    fo := times(80,2);
    write(fo)
end
.