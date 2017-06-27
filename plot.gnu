set xlabel "Pathsize"
set xtics (2, 5, 10, 20, 50, 100)
set ylabel "HistoryCount"
set ytics 2,1,6
set zlabel "Error Rate"
set zrange [0.5:0.05]

#set pm3d map
set pm3d interpolate 10,10
set hidden3d
set datafile separator ";"
set dgrid3d 20,20
splot "resultMatrix5er.csv" u 1:2:3 with pm3d notitle
pause -1
