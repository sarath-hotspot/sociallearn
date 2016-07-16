package com.sociallearn.backend.api;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.DefaultValue;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.google.appengine.repackaged.com.google.common.collect.ArrayListMultimap;
import com.googlecode.objectify.Key;
import com.sociallearn.backend.OfyService;
import com.sociallearn.backend.bean.InterestListWrapper;
import com.sociallearn.backend.bean.StartupApiStatus;
import com.sociallearn.backend.bean.StartupSummary;
import com.sociallearn.backend.bean.StartupSummaryListWrapper;
import com.sociallearn.backend.db.Interest;
import com.sociallearn.backend.db.StartupDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sarath on 16-07-2016.
 */
@Api(
        name = "startupApi",
        version = "v1",
        resource = "user",
        namespace = @ApiNamespace(
                ownerDomain = "db.backend.sociallearn.com",
                ownerName = "db.backend.sociallearn.com",
                packagePath = ""
        )
)
public class StartupEndpoint {

    private static final String DEFAULT_BATCH_START = "0";
    private static final String DEFAULT_BATCH_SIZE = "10";

    @ApiMethod(name = "getInterests",
            path = "getInterests",
            httpMethod = ApiMethod.HttpMethod.GET)
    public InterestListWrapper getInterests(
            @Named("batchStart") @Nullable @DefaultValue(DEFAULT_BATCH_START) Integer batchStart,
            @Named("batchSize") @Nullable @DefaultValue(DEFAULT_BATCH_SIZE) Integer batchSize) {

        List<Interest> interests = new ArrayList<>(
                OfyService.ofy()
                        .load()
                        .type(Interest.class)
                        .offset(batchStart)
                        .limit(batchSize)
                        .list());
        return new InterestListWrapper(interests);
    }


    @ApiMethod(name= "getStartupsByInterest",
               path = "getStartupsByInterest",
                httpMethod = ApiMethod.HttpMethod.GET)
    public StartupSummaryListWrapper getStartupsByInterest(
            @Named("interestId") Long interestId,
            @Named("batchStart") @Nullable @DefaultValue(DEFAULT_BATCH_START) Integer batchStart,
            @Named("batchSize") @Nullable @DefaultValue(DEFAULT_BATCH_START) Integer batchSize)
    {
        List<StartupDetails> startDetails = OfyService.ofy()
                .load()
                .type(StartupDetails.class)
                .filter("interestId = ", interestId)
                .offset(batchStart)
                .limit(batchSize)
                .list();
        List<StartupSummary> result = new ArrayList<>();
        for (StartupDetails sd : startDetails)
        {
            StartupSummary sm = new StartupSummary();
            sm.setStartupId(sd.getStartupId());
            sm.setStartupName(sd.getStartupName());
            sm.setStartupSmallDesc(sd.getStartupSmallDesc());
            sm.setIconUrl(sd.getIconUrl());
            sm.setAndroidPackage(sd.getAndroidPackageId());
            result.add(sm);
        }

        return new StartupSummaryListWrapper(result);
    }


    @ApiMethod(name = "getStartupDetails",
                path = "getStartupDetails",
                httpMethod = ApiMethod.HttpMethod.GET)
    public StartupDetails getStartupDetailsById(@Named("startupId") Long startupId)
    {
        StartupDetails startupDetails = OfyService.ofy().load().key(Key.create(StartupDetails.class, startupId)).now();
        if (startupDetails == null)
        {
            throw new RuntimeException("Startup with id " + startupId + " not found");
        }
        return startupDetails;
    }

    @ApiMethod(name = "addInterest", path = "addInterest", httpMethod = ApiMethod.HttpMethod.POST)
    public StartupApiStatus addInterest(
            @Named("name") String name,
            @Named("imageUrl") String imageUrl)
    {
        Interest i = new Interest();
        i.setInterestName(name);
        i.setImageUrl(imageUrl);
        OfyService.ofy().save().entity(i).now();
        return new StartupApiStatus("Success");
    }

    @ApiMethod(name = "addStartup", path = "addStartup", httpMethod = ApiMethod.HttpMethod.POST)
    public StartupApiStatus addStartup(@Named("interestId") Long interestId,
                           @Named("startupName") String startupName,
                           @Named("startupSmallDesc") String startupSmallDesc,
                           @Named("startupDesc") String startupDesc,
                           @Named("startupIconUrl") String startupIconUrl,
                           @Named("startupBannerUrl") String startupBannerUrl,
                           @Named("startupAndroidPackageId") String androidPackageId,
                                       @Named("rewardStatement") String rewardStatement,
                                       @Named("rewardAmount") Double rewardAmount)
    {

        // Validate interest id.
        Interest interest = OfyService.ofy().load().key(Key.create(Interest.class, interestId)).now();
        if (interest == null) {
            throw new RuntimeException("Invalid interest id " + interestId);
        }

        StartupDetails sd = new StartupDetails();
        sd.setInterestId(interestId);
        sd.setStartupName(startupName);
        sd.setStartupSmallDesc(startupSmallDesc);
        sd.setStartupDescription(startupDesc);
        sd.setIconUrl(startupIconUrl);
        sd.setBannerUrl(startupBannerUrl);
        sd.setAndroidPackageId(androidPackageId);
        sd.setRewardStatement(rewardStatement);
        sd.setRewardAmount(rewardAmount);
        OfyService.ofy().save().entity(sd).now();

        return new StartupApiStatus("Success");
    }
}
