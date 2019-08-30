/**
 *
 */
package org.devgateway.ocds.persistence.mongo.flags;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;

/**
 * @author mpostelnicu Represents the list of red flags at the Release level
 */
public class ReleaseFlags implements FlagsWrappable, Serializable {

    // i038: Competitive tender w/ short bidding period
    private Flag i038;

    //i007 This awarded competitive tender only featured a single bid`
    private Flag i007;

    //i004: Sole source award above the threshold
    private Flag i004;

    //i019: High number of contract awards to one supplier within a given time period by a single procurement entity
    private Flag i077;

    //i077: Long delays in contract negotiations or award
    private Flag i019;

    //i180: Contractor receives multiple single-source/non-competitive contracts from a single procuring entity
    // during a defined time period
    private Flag i180;

    //i002: Winning supplier provides a substantially lower bid price than competitors
    private Flag i002;

    //i085: Bids are an exact percentage apart
    private Flag i085;

    //i171: Bid is too close to budget, estimate or preferred solution
    private Flag i171;

    //i184: Difference between the contract price and winning bid
    private Flag i184;

    //i016 Purchase splitting to avoid procurement thresholds
    private Flag i016;

    //i045 Supplier wins disproportionate number of contracts of the same type
    private Flag i045;

    //i182 Lowest evaluated bidder is considered non-responsive
    private Flag i182;

    //i083 The same companies always bid, the same companies always win, and the same companies always lose
    private Flag i083;

    private Collection<FlagTypeCount> flaggedStats;

    private Collection<FlagTypeCount> eligibleStats;

    //total number of indicators flagged for this release

    private Integer totalFlagged;

    @Transient
    private Integer flagCnt;

    @Transient
    private HashMap<FlagType, FlagTypeCount> flaggedStatsMap = new HashMap<>();

    @Transient
    private HashMap<FlagType, FlagTypeCount> eligibleStatsMap = new HashMap<>();


    public Collection<FlagTypeCount> getFlaggedStats() {
        return flaggedStats;
    }

    public void setFlaggedStats(Collection<FlagTypeCount> flaggedStats) {
        this.flaggedStats = flaggedStats;
    }

    public Collection<FlagTypeCount> getEligibleStats() {
        return eligibleStats;
    }

    public void setEligibleStats(Collection<FlagTypeCount> eligibleStats) {
        this.eligibleStats = eligibleStats;
    }

    @Override
    public HashMap<FlagType, FlagTypeCount> getFlaggedStatsMap() {
        return flaggedStatsMap;
    }

    public void setFlaggedStatsMap(HashMap<FlagType, FlagTypeCount> flaggedStatsMap) {
        this.flaggedStatsMap = flaggedStatsMap;
    }

    @Override
    public HashMap<FlagType, FlagTypeCount> getEligibleStatsMap() {
        return eligibleStatsMap;
    }

    public void setEligibleStatsMap(HashMap<FlagType, FlagTypeCount> eligibleStatsMap) {
        this.eligibleStatsMap = eligibleStatsMap;
    }

    public Integer getTotalFlagged() {
        return totalFlagged;
    }

    public void setTotalFlagged(Integer totalFlagged) {
        this.totalFlagged = totalFlagged;
    }

    public Flag getI019() {
        return i019;
    }

    public void setI019(Flag i019) {
        this.i019 = i019;
    }

    public Flag getI004() {
        return i004;
    }

    public void setI004(Flag i004) {
        this.i004 = i004;
    }

    public Flag getI038() {
        return i038;
    }

    public void setI038(Flag i038) {
        this.i038 = i038;
    }

    public Flag getI007() {
        return i007;
    }

    public void setI007(Flag i007) {
        this.i007 = i007;
    }

    public Flag getI077() {
        return i077;
    }

    public void setI077(Flag i077) {
        this.i077 = i077;
    }

    public Flag getI180() {
        return i180;
    }

    public void setI180(Flag i180) {
        this.i180 = i180;
    }

    public Flag getI002() {
        return i002;
    }

    public void setI002(Flag i002) {
        this.i002 = i002;
    }

    public Flag getI085() {
        return i085;
    }

    public void setI085(Flag i085) {
        this.i085 = i085;
    }

    public Flag getI171() {
        return i171;
    }

    public void setI171(Flag i171) {
        this.i171 = i171;
    }

    public Integer getFlagCnt() {
        return flagCnt;
    }

    public void setFlagCnt(Integer flagCnt) {
        this.flagCnt = flagCnt;
    }

    public void incFlagCnt() {
        if (flagCnt == null) {
            flagCnt = 1;
        } else {
            flagCnt++;
        }
    }

    public Flag getI184() {
        return i184;
    }

    public void setI184(Flag i184) {
        this.i184 = i184;
    }

    public Flag getI016() {
        return i016;
    }

    public void setI016(Flag i016) {
        this.i016 = i016;
    }

    public Flag getI045() {
        return i045;
    }

    public void setI045(Flag i045) {
        this.i045 = i045;
    }

    public Flag getI182() {
        return i182;
    }

    public void setI182(Flag i182) {
        this.i182 = i182;
    }

    public Flag getI083() {
        return i083;
    }

    public void setI083(Flag i083) {
        this.i083 = i083;
    }

    /**
     * Return the flag's name as upper case, from flag path, this can be used to access the flag property directly from
     * {@link ReleaseFlags} object
     *
     * @param fullFlagPath
     * @return
     */
    private static String getShortFlagPropUpper(String fullFlagPath) {
        return fullFlagPath.replaceAll("flags.", "");
    }

    public boolean getFlagSet(String fullFlagPath) {
        try {
            return "true".equals(BeanUtils.getProperty(this, getShortFlagPropUpper(fullFlagPath)));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

}

