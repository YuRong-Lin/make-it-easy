# CPU 上下文切换

### 源起
在导致系统负载升高的场景中，多进程竞争cpu就是其中一个。但竞争cpu并没有真正运行，为什么会导致负载升高呢？其幕后“黑手”就是--cpu上下文切换。


### 定义
Linux是一个多任务操作系统，支持远大于cpu数量的任务同时运行。当然“同时运行”只是一种错觉，实际是系统在很短时间内，将cpu轮流分配给它们的结果。任务运行前，cpu需要知道从哪里加载，又从哪里运行，
这些信息维护在各种**寄存器**和**程序计数器**（Program Counter，PC）中，它们是cpu运行任务的必要环境，因此也被叫做**cpu上下文**。

显然，cpu上下文切换就是在任务切换时，将前一任务的上下文保存下来，然后加载新任务的上下文，然后跳到新任务的指定代码处运行。这个过程就是“cpu上下文切换”，显然，它是有成本的。但不同上下文切换成本不同，需分场景具体分析。


### 场景

#### （一）特权级模式切换
##### 用户态和内核态
  进程可运行在**用户空间**和**内核空间**，在用户空间运行时称为进程的**用户态**，对应cpu特权等级ring 3，在内核空间运行时称为进程的**内核态**，对应cpu特权等级ring 0。
  
    内核空间（Ring 0）具有最高权限，可以直接访问所有资源；
    用户空间（Ring 3）只能访问受限资源，不能直接访问内存等硬件设备，必须通过系统调用陷入到内核中，才能访问这些特权资源。
  
  用户态到内核态的转变，需要通过**系统调用**。发起系统调用时，cpu寄存器保存的是用户态的指令位置，为了执行内核指令，cpu寄存器需要更新为内核指令的新位置，然后跳转到内核态执行任务。系统调用结束后，
  cpu寄存器需要恢复原来保存的用户态，切换到用户空间，继续运行进程。这个过程涉及**两次cpu上下文切换**。
  
    注：系统调用不涉及虚拟内存等用户态进程资源，也不切换进程（一直都是同一个进程在运行），这是跟进程上下文切换的重要区别。
  
  
#### （二）进程上下文切换
  进程是系统资源分配的基本单位，由内核管理和调度，即**进程切换只能发生在内核态**。进程的上下文不仅包括虚拟内存、栈、全局变量等用户空间资源，还包括内核堆栈、寄存器等内核空间状态。
  
  相比系统调用，进程上下文切换在保存当前进程的内核状态和cpu寄存器之前，需要先将该进程的虚拟内存、栈等保存下来；而加载了下一进程的内核态后，还需要刷新进程的虚拟内存和用户栈。
  这个成本是可观的。当产生大量的进程上下文切换时，会导致cpu将大量时间消耗在寄存器、内核栈和虚拟内存等资源的保存和恢复上，而大大减少了真正运行进程的时间，从而导致平均负载升高。
  
  此外，linux通过TLB（Translation Lookaside Buffer）来管理虚拟内存到物理内存的映射关系，当虚拟内存更新后，TLB也要刷新，内存访问变慢。特别是在多处理器系统上，缓存是被多个处理器共享的，
  刷新缓存不仅会影响当前处理器的进程，还会影响共享缓存的其他处理器的进程。
  
##### 进程上下文切换场景
* 进程执行完毕，系统从就绪队列中调度新进程
* 进程cpu时间片用完，被调度系统切换
* 进程遇到系统资源不足（如内存资源不足）时，需等待资源满足后才能运行，这时进程会被挂起，并由系统调度其他进程运行
* 进程调用睡眠函数sleep方法主动挂起自己，系统重新调度
* 当更高优先级进程运行时，当前进程被挂起，由更高优先级进程先运行
* 当发生硬件中断时，cpu上的当前进程会被挂起，转而执行内核中的中断服务程序


#### （三）线程上下文切换
  线程跟进程的最大区别是：**线程是调度的基本单位，而进程是资源拥有的基本单位**。所谓内核中的任务调度，实际上的调度对象是线程；而进程只是给线程提供了虚拟内存、全局变量等资源。
  
  因此，可以这么理解：
  
    当进程只有1个线程时，可以认为进程就等于线程；
    当进程有多个线程时，这些线程会共享相同的虚拟内存、全局变量等资源，这些资源在线程上下文切换时不需要修改；
    另外，线程也是有拥有私有数据的，比如栈和寄存器等，这些在上下文切换时是需要保存的。
    
  因此线程上下文切换可分为两种情况：
  
    第一种，前后两个线程属于不同的进程，由于资源不共享，此时切换过程跟进程上下文切换是一样的；
    第二种，前后两个线程属于同一个进程，这时虚拟内存等共享资源保持不变，只需要保持线程的私有数据、寄存器等不共享的资源。
  
  可以看出，虽然同为上下文切换，但同进程内的线程切换，要比多进程间的切换消耗更少的资源，而这，也正是多线程代替多进程的一个优势。

#### （三）中断上下文切换
  为了快速响应硬件的事件，中断处理会打断进程的正常调度和执行，转而调用中断处理程序，响应设备事件。而在打断其他进程时，需要将进程的当前状态保存下来，等待中断处理结束后，
  进程可以从原来的状态恢复运行。
  
  跟进程上下文不同，**中断上下文切换并不涉及到进程的用户态**。所以，即便中断过程打断了一个正处在用户态的进程，也不需要保存和恢复这个进程的虚拟内存、全局变量等用户态资源。
  **中断上下文，其实只包括内核态中断服务程序执行所必需的状态，包括CPU 寄存器、内核堆栈、硬件中断参数等**。

  **对同一个 CPU 来说，中断处理比进程拥有更高的优先级，所以中断上下文切换并不会与进程上下文切换同时发生**。相应的，为了尽可能快的执行结束，大部分中断处理程序都短小精悍。

  另外，中断上下文切换同样会消耗CPU，切换次数过多也会耗费大量的 CPU，甚至严重降低系统的整体性能。所以当发现中断次数过多时，就需要注意去排查它是否会给系统带来严重的性能问题。

### 查看系统上下文切换情况
工具：vmstat 和 pidstat
  
vmstat示例：
  
    # 每隔5秒输出1组数据
    $ vmstat 5
    procs -----------memory---------- ---swap-- -----io---- -system-- ------cpu-----
     r  b   swpd   free   buff  cache   si   so    bi    bo   in   cs us sy id wa st
     0  0      0 7005360  91564 818900    0    0     0     0   25   33  0  0 100  0  0
 
 重要的四列：    
 * cs（context switch）是每秒上下文切换的次数。
 * in（interrupt）则是每秒中断的次数。
 * r（Running or Runnable）是就绪队列的长度，也就是正在运行和等待CPU的进程数。
 * b（Blocked）则是处于不可中断睡眠状态的进程数。
 
 vmstat只显示系统的总体情况，具体查看某个进程，需用pidstat（-w 查看进程切换情况）：
 
    # 每隔5秒输出1组数据
    $ pidstat -w 5
    Linux 4.15.0 (ubuntu)  09/23/18  _x86_64_  (2 CPU)
    
    08:18:26      UID       PID   cswch/s nvcswch/s  Command
    08:18:31        0         1      0.20      0.00  systemd
    08:18:31        0         8      5.40      0.00  rcu_sched
    ...
    
重要的2列：
* 一个是 cswch ，表示每秒自愿上下文切换（voluntary context switches）的次数；
* 另一个则是 nvcswch ，表示每秒非自愿上下文切换（non voluntary context switches）的次数

重要概念：
* 所谓自愿上下文切换，是指**进程无法获取所需资源，导致的上下文切换**。比如说， I/O、内存等系统资源不足时，就会发生自愿上下文切换。
* 而非自愿上下文切换，则是指**进程由于时间片已到等原因，被系统强制调度，进而发生的上下文切换**。比如说，大量进程都在争抢 CPU 时，就容易发生非自愿上下文切换。

### 实战分析（Ubuntu 18.04）

工具：sysbench/sysstat

空闲状态：

    # 间隔1秒后输出1组数据
    $ vmstat 1 1
    procs -----------memory---------- ---swap-- -----io---- -system-- ------cpu-----
     r  b   swpd   free   buff  cache   si   so    bi    bo   in   cs us sy id wa st
     0  0      0 6984064  92668 830896    0    0     2    19   19   35  1  0 99  0  0
     
模拟多线程：

    # 以10个线程运行5分钟的基准测试，模拟多线程切换的问题
    $ sysbench --threads=10 --max-time=300 threads run
    
观察：

    # 每隔1秒输出1组数据（需要Ctrl+C才结束）
    $ vmstat 1
    procs -----------memory---------- ---swap-- -----io---- -system-- ------cpu-----
     r  b   swpd   free   buff  cache   si   so    bi    bo   in   cs us sy id wa st
     6  0      0 6487428 118240 1292772    0    0     0     0 9019 1398830 16 84  0  0  0
     8  0      0 6487428 118240 1292772    0    0     0     0 10191 1392312 16 84  0  0  0
     
cs 列的上下文切换次数从之前的 35 骤然上升到了 139 万。同时，注意观察其他几个指标：

* r 列：就绪队列的长度已经到了 8，远远超过了系统 CPU 的个数 2，所以肯定会有大量的 CPU 竞争。
* us（user）和 sy（system）列：这两列的CPU 使用率加起来上升到了 100%，其中系统 CPU 使用率，也就是 sy 列高达 84%，说明 CPU 主要是被内核占用了。
* in 列：中断次数也上升到了1万左右，说明中断处理也是个潜在的问题。

这几个指标指出，系统的就绪队列过长，也就是正在运行和等待CPU的进程数过多，导致了大量的上下文切换，而上下文切换又导致了系统 CPU 的占用率升高

分析：

    # 每隔1秒输出1组数据（需要 Ctrl+C 才结束）
    # -w参数表示输出进程切换指标，而-u参数则表示输出CPU使用指标
    $ pidstat -w -u 1
    08:06:33      UID       PID    %usr %system  %guest   %wait    %CPU   CPU  Command
    08:06:34        0     10488   30.00  100.00    0.00    0.00  100.00     0  sysbench
    08:06:34        0     26326    0.00    1.00    0.00    0.00    1.00     0  kworker/u4:2
    
    08:06:33      UID       PID   cswch/s nvcswch/s  Command
    08:06:34        0         8     11.00      0.00  rcu_sched
    08:06:34        0        16      1.00      0.00  ksoftirqd/1
    08:06:34        0       471      1.00      0.00  hv_balloon
    08:06:34        0      1230      1.00      0.00  iscsid
    08:06:34        0      4089      1.00      0.00  kworker/1:5
    08:06:34        0      4333      1.00      0.00  kworker/0:3
    08:06:34        0     10499      1.00    224.00  pidstat
    08:06:34        0     26326    236.00      0.00  kworker/u4:2
    08:06:34     1000     26784    223.00      0.00  sshd
    
从pidstat的输出可发现，CPU 使用率的升高果然是 sysbench 导致的，它的 CPU 使用率已经达到了 100%。但上下文切换则是来自其他进程，包括非自愿上下文切换频率最高的 pidstat ，以及自愿上下文切换频率最高的内核线程 kworker 和 sshd。

不过，怪异的是pidstat 输出的上下文切换次数，加起来也就几百，比 vmstat 的 139 万明显小了太多。这是怎么回事呢？

这是因为Linux 调度的基本单位实际上是线程，而该场景中 sysbench 模拟的也是线程的调度问题，而pidstat 默认显示进程的指标数据，加上 -t 参数后，才会输出线程的指标。

    # 每隔1秒输出一组数据（需要 Ctrl+C 才结束）
    # -wt 参数表示输出线程的上下文切换指标
    $ pidstat -wt 1
    08:14:05      UID      TGID       TID   cswch/s nvcswch/s  Command
    ...
    08:14:05        0     10551         -      6.00      0.00  sysbench
    08:14:05        0         -     10551      6.00      0.00  |__sysbench
    08:14:05        0         -     10552  18911.00 103740.00  |__sysbench
    08:14:05        0         -     10553  18915.00 100955.00  |__sysbench
    08:14:05        0         -     10554  18827.00 103954.00  |__sysbench
    
 可以看到虽然 sysbench 进程（也就是主线程）的上下文切换次数看起来并不多，但它的子线程的上下文切换次数却有很多。看来，上下文切换罪魁祸首，还是过多的 sysbench 线程。
 
 中断次数升高分析
 
 需要从 /proc/interrupts 这个只读文件中读取信息
 
    # -d 参数表示高亮显示变化的区域
    $ watch -d cat /proc/interrupts
               CPU0       CPU1
    ...
    RES:    2450431    5279697   Rescheduling interrupts
    ...
    
观察一段时间，可以发现变化速度最快的是重调度中断（RES），这个中断类型表示，唤醒空闲状态的 CPU 来调度新的任务运行。这是多处理器系统（SMP）中，调度器用来分散任务到不同 CPU 的机制，通常也被称为处理器间中断（Inter-Processor Interrupts，IPI）。

所以，这里的中断升高还是因为过多任务的调度问题，跟前面上下文切换次数的分析结果是一致的。

### 总结

* 自愿上下文切换变多了，说明进程都在等待资源，有可能发生了 I/O 等其他问题；
* 非自愿上下文切换变多了，说明进程都在被强制调度，也就是都在争抢 CPU，说明 CPU 的确成了瓶颈；
* 中断次数变多了，说明 CPU 被中断处理程序占用，还需要通过查看 /proc/interrupts 文件来分析具体的中断类型。
