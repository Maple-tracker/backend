package com.mmt.tracker.market.service;

import com.mmt.tracker.market.controller.dto.request.CompleteItemNameGetRequest;
import com.mmt.tracker.market.controller.dto.response.CompleteItemNameResponse;
import com.mmt.tracker.market.domain.ItemName;
import com.mmt.tracker.market.util.HangulUtil;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemSearchService {

    // 완성된 한글 글자 확인을 위한 정규표현식
    private static final String COMPLETE_SYLLABLE_REGEX = "^[가-힣]+$";

    public CompleteItemNameResponse getCompleteItemNames(CompleteItemNameGetRequest request) {
        String userInput = request.input();
        String trimmedUserInput = HangulUtil.removeSpaces(userInput);

        if (trimmedUserInput.matches(COMPLETE_SYLLABLE_REGEX)) {
            return new CompleteItemNameResponse(getItemNamesFromPiece(trimmedUserInput));
        }

        return new CompleteItemNameResponse(getItemNamesFromInitial(HangulUtil.getInitial(trimmedUserInput)));
    }

    private List<String> getItemNamesFromPiece(String pieces) {
        return Stream.of(ItemName.values())
                .map(ItemName::getValue)
                .filter(value -> HangulUtil.removeSpaces(value).contains(pieces))
                .toList();
    }

    private List<String> getItemNamesFromInitial(String initials) {
        return Stream.of(ItemName.values())
                .map(ItemName::getValue)
                .filter(value -> HangulUtil.getInitial(HangulUtil.removeSpaces(value)).contains(initials))
                .toList();
    }
}
