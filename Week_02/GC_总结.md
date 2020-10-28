1. 基础

![图片](https://uploader.shimo.im/f/pzkaHOa8Dh2eQeVz.png!thumbnail)

4种GC实验

wrk -t8  -c40 -d60s[http://localhost:8088/api/hello](http://localhost:8088/api/hello)

电脑为4核 分8个线程跑

1. 串行GC  （128M）

![图片](https://uploader.shimo.im/f/mSQ0u5emBJQYgxDS.jpeg!thumbnail)![图片](https://uploader.shimo.im/f/QZGHMNRN2QbxXqCy.jpeg!thumbnail)

（**512M**）

![图片](https://uploader.shimo.im/f/IlUQjgFw3x9dVtAS.jpeg!thumbnail)![图片](https://uploader.shimo.im/f/Hwl8IFwFjZB8WUig.jpeg!thumbnail)

（2G）

![图片](https://uploader.shimo.im/f/FiTjZJdEq5A4V2Ue.jpeg!thumbnail)![图片](https://uploader.shimo.im/f/6zDmgHh49fVm0W5I.jpeg!thumbnail)

4G

![图片](https://uploader.shimo.im/f/f6FZinZyeAMktuEK.jpeg!thumbnail)![图片](https://uploader.shimo.im/f/vyMEF8h37acYKvzc.jpeg!thumbnail)


2 并行GC

128M

![图片](https://uploader.shimo.im/f/30RJifT1gDnCqkar.jpeg!thumbnail)![图片](https://uploader.shimo.im/f/nLbmHOKbt5imr1b1.jpeg!thumbnail)

512M

![图片](https://uploader.shimo.im/f/1yozXr74BF6qSzqx.jpeg!thumbnail)![图片](https://uploader.shimo.im/f/AJiZxwJ60ckKKdNL.jpeg!thumbnail)

2G

![图片](https://uploader.shimo.im/f/jkvf6iXvHqAoq0pQ.jpeg!thumbnail)![图片](https://uploader.shimo.im/f/zdTEM609DuOYbXr2.jpeg!thumbnail)

4G

![图片](https://uploader.shimo.im/f/LxImnMsFwEy0u8Hn.jpeg!thumbnail)![图片](https://uploader.shimo.im/f/K7aRSJJXAwYovN9d.jpeg!thumbnail)

3 CMS

128M

![图片](https://uploader.shimo.im/f/V66BHmrhzdEVxsIo.jpeg!thumbnail)![图片](https://uploader.shimo.im/f/fOctvy2xmJY4qGhn.jpeg!thumbnail)

512M

![图片](https://uploader.shimo.im/f/F1KOQ0su30XvGaWo.jpeg!thumbnail)![图片](https://uploader.shimo.im/f/D6lmunnuOlTIMDC8.jpeg!thumbnail)

2G

![图片](https://uploader.shimo.im/f/JG85kCfeOE00P2g8.jpeg!thumbnail)![图片](https://uploader.shimo.im/f/TLfMvubiJZvZjMaY.jpeg!thumbnail)


4G

![图片](https://uploader.shimo.im/f/r6jwJdGqO58s6SKE.jpeg!thumbnail)![图片](https://uploader.shimo.im/f/8swAQSj5WyPXvYWQ.jpeg!thumbnail)

1. G1

128M

![图片](https://uploader.shimo.im/f/NaJX3p8FPkpsN33z.jpeg!thumbnail)![图片](https://uploader.shimo.im/f/GQdq44mryObFVGFE.jpeg!thumbnail)

512M

![图片](https://uploader.shimo.im/f/xLvaWKVSZsevk3G4.jpeg!thumbnail)![图片](https://uploader.shimo.im/f/ffKZxv6l9Ntd3clq.jpeg!thumbnail)

2G

![图片](https://uploader.shimo.im/f/9ftwHXFSSyp3lOee.jpeg!thumbnail)![图片](https://uploader.shimo.im/f/rnDPLnl9EmEoFU3P.jpeg!thumbnail)

4G

![图片](https://uploader.shimo.im/f/oFSnRvdl26BEexSP.jpeg!thumbnail)![图片](https://uploader.shimo.im/f/yNTBYAHF1EcdfAIp.jpeg!thumbnail)

总结

1. 内存分配越大， GC次数越少， 但是gc时间变长
2. 多核 并行GC优与串行
3. CMS延迟相对并行小
4. G1 综合优于CMS
5. 分配内存不是越大越好，通过上面实验 分配512M性能最好尤其G1 达到4万多
