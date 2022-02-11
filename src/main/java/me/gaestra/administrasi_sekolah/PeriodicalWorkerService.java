/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gaestra.administrasi_sekolah;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import me.gaestra.administrasi_sekolah.helper.UtilityMiscHelper;
import me.gaestra.administrasi_sekolah.model.Siswa;
import me.gaestra.administrasi_sekolah.model.Task;
import me.gaestra.administrasi_sekolah.model.Tunjangan;

/**
 *
 * @author Ganesha
 */
public class PeriodicalWorkerService {
    
    // Core Instance Component
    private static PeriodicalWorkerService instance;
    
    // Core Local Component
    private boolean sigStop = false;
    private Thread mainThread = new Thread();
    
    public static PeriodicalWorkerService getInstance() {
        if (instance == null)
            instance = new PeriodicalWorkerService();
        
        return instance;
    }
    
    private PeriodicalWorkerService() {
        Task.InitializeTable();
    }
    
    public void start() {
        sigStop = false;
        initServiceWorker();
    }
    
    public void stop() { 
        sigStop = true;
        try { mainThread.join(); } catch (Exception e) { e.printStackTrace(); }
    }
    
    private void initServiceWorker() {
        mainThread = new Thread(){
            public void run(){
                try { doWork(); } catch (Exception e) { e.printStackTrace(); }
            }
        };
        
        mainThread.start();
    }
    
    private void doWork() throws InterruptedException {
        while(true) {
            Thread.sleep(5000); // Execute every 5 second
            
            if (sigStop)
                break;
            
            for (var tunj : Tunjangan.all()) {
                if (tunj.periodic_mode == Tunjangan.PeriodicMode.NONE)
                    continue;
                
                boolean store = false;
                Task task = this.searchTaskContext(tunj.id);
                
                if (task == null) {
                    task = new Task();
                    store = true;
                }
                LocalDate dateExecuted = LocalDate.now();
                
                LocalDate periodDate = UtilityMiscHelper.formatToDate(tunj.first_date);
                LocalDate graceDate = UtilityMiscHelper.formatToDate(tunj.second_date);
                
                if (!store) {
                    dateExecuted = UtilityMiscHelper.formatToDate(task.executed_at);

                    boolean abort = false;
                    switch (tunj.periodic_mode) {
                        case Tunjangan.PeriodicMode.MONTHLY:
                        case Tunjangan.PeriodicMode.MONTHLY_SPECIFIED:
                            abort = dateExecuted.getMonthValue() == LocalDate.now().getMonthValue() 
                                    || LocalDate.now().getDayOfMonth() != periodDate.getDayOfMonth();
                            break;
                        case Tunjangan.PeriodicMode.YEARLY:
                        case Tunjangan.PeriodicMode.YEARLY_SPECIFIED:
                            abort = dateExecuted.getYear() == LocalDate.now().getYear()
                                    || LocalDate.now().getMonthValue() != periodDate.getMonthValue()
                                    || LocalDate.now().getDayOfMonth() != periodDate.getDayOfMonth();
                            break;
                        case Tunjangan.PeriodicMode.ONDATE:
                        case Tunjangan.PeriodicMode.ONDATE_SPECIFIED:
                            abort = dateExecuted.isEqual(LocalDate.now()) || periodDate.isEqual(LocalDate.now());
                            break;
                        default: break;
                    }

                    if (abort) 
                        continue;
                    
                }

                for (var siswa : Siswa.all()) {
                    if (tunj.periodic_mode > 3 && siswa.kelas_id != tunj.specific_id)
                        continue;

                    tunj.issued_date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
                    tunj.grace_date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().plusDays(graceDate.getDayOfMonth() - periodDate.getDayOfMonth()));
                    siswa.tunjangan.add(tunj);
                    siswa.updateTunjangan();
                }

                task.tunjangan_id = tunj.id;
                task.executed_at = DateTimeFormatter.ofPattern(UtilityMiscHelper.DateFormat.DATE_STRING_FORMAT_3_REVERSED).format(LocalDate.now());

                task.save();
            }
        }
    }
    
    private Task searchTaskContext(int tunjangan_id) {
        return Task.get(tunjangan_id);
    }
}
