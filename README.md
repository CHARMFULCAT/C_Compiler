SIMPLE MINI C COMPILER



实现了词法分析、语义分析以及语法分析，最后生成三地址中间码

如

```C
{
    int j;
    int i;
    int[100] a;
    while(true){
        do{
            j = j - 1;
        }while(j < a[j]);
        break;
    }
    j = j + 2;
    do{
        j = j + 10;
    }while(i < j);
}
```

生成

```
L1:L4:  j = j - 1
L6:     t1 = j * 4
        t2 = a [ t1 ]
        if j < t2 goto L4
L5:     goto L3
        goto L1
L3:     j = j + 2
L7:     j = j + 10
L8:     if i < j goto L7
L2:
```



使用方式：
在...\C_Compiler\src\ 打开cmd，在cmd中输入`java main.Main 需要编译的源文件名`

为了便于显示我直接在命令行中输出结果了，之后可以在lexer文件中的Lexer.java中将输出方式修改为输出到某个文件





只能处理被大括号括着的block

按照正确的C语法输入（`int main(){...}`）甚至可能报错

但是能实现一部分编译的功能

