# MockService
在做微服务的时候，常常会出现A服务依赖B服务，而很可能这个时候B服务并没有完成。那么我们如何进行B服务的测试呢？最直接的想法即是做一个假的B服务用来模拟B服务的功能。

而MockService的主要功能即是去模拟一个未完成的假服务。

### 代码块
``` python
@requires_authorization
def somefunc(param1='', param2=0):
    '''A docstring'''
    if param1 > param2: # interesting
        print 'Greater'
    return (param2 - param1 + 1) or None
class SomeClass:
    pass
>>> message = '''interpreter
... prompt'''
```
### LaTeX 公式
$$	x = \dfrac{-b \pm \sqrt{b^2 - 4ac}}{2a} $$

### 表格
| Item      |    Value | Qty  |
| :-------- | --------:| :--: |
| Computer  | 1600 USD |  5   |
| Phone     |   12 USD |  12  |
| Pipe      |    1 USD | 234  |

### 流程图
```flow
st=>start: Start
e=>end
op=>operation: My Operation
cond=>condition: Yes or No?

st->op->cond
cond(yes)->e
cond(no)->op
```

以及时序图:

```sequence
Alice->Bob: Hello Bob, how are you?
Note right of Bob: Bob thinks
Bob-->Alice: I am good thanks!
```
## 反馈与建议
- 邮箱:<lear521@163.com>

